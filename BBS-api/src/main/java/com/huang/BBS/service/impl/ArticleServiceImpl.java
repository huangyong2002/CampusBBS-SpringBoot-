package com.huang.BBS.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huang.BBS.dao.dos.Archives;
import com.huang.BBS.dao.mapper.ArticleBodyMapper;
import com.huang.BBS.dao.mapper.ArticleMapper;
import com.huang.BBS.dao.mapper.ArticleTagMapper;
import com.huang.BBS.dao.pojo.Article;
import com.huang.BBS.dao.pojo.ArticleBody;
import com.huang.BBS.dao.pojo.ArticleTag;
import com.huang.BBS.dao.pojo.SysUser;
import com.huang.BBS.service.ArticleService;
import com.huang.BBS.service.SysUserService;
import com.huang.BBS.service.TagService;
import com.huang.BBS.service.ThreadService;
import com.huang.BBS.utils.UserThreadLocal;
import com.huang.BBS.vo.ArticleBodyVo;
import com.huang.BBS.vo.ArticleVo;
import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.TagVo;
import com.huang.BBS.vo.params.ArticleParam;
import com.huang.BBS.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired(required = false)
    private ArticleMapper articleMapper;
    @Autowired(required = false)
    private TagService tagService;
    @Autowired(required = false)
    private SysUserService sysUserService;
    @Autowired(required = false)
    private ArticleTagMapper articleTagMapper;



//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1.分页查询 article 数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        //查询文章的参数 加上分类id，判断不为空 加上分类条件
//        //and cate_gory_id=#{categoryId}
//        if (pageParams.getCategoryId() != null) {
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null){
//            //加入标签 条件查询
//            //article表中 并没有tag字段 一篇文章 有多个biaoqian
//            //article_tag  article_id 1:n tag_id
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0){
//                //and id in(1,2,3)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//
//
//        //是否置顶进行排序 文章的权重是否为1
//        //按时间倒序
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        //将数据库中的数据进行一次封装
//        List<ArticleVo> articleVoList=copyList(records,true,true);
//        return Result.success(articleVoList);
//    }


    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }


















    @Override
    public Result hotarticles(int limit) {

        //select * id,title from order by view_counts desc limit 5;

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);


        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));

    }

    @Override
    public Result newarticles(int limit) {

        //select * id,title from order by create_date desc limit 5;

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);


        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }



    @Override
    public Result listArchives() {

        List<Archives> ArchivesList =articleMapper.listArchives();
        return Result.success(ArchivesList);
    }





    @Autowired(required = false)
    private ThreadService threadService;
    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1.根据id查询文章
         * 2.根据bodyId和categoryid 去做关联查询
         */

        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo=copy(article,true,true,true,true);
        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁,阻塞其他读操作，性能就会比较低
        //更新 增加了此次接口的耗时 如果一旦跟新出现了问题，不能影响 查看文章的操作
        //线程池    可以把更新操作 扔到线程池中去执行， 和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Transactional
    @Override
    public Result publish(ArticleParam articleParam) {
        //此接口要加入登录拦截当中
        SysUser sysUser = UserThreadLocal.get();

        /**
         * 1.发布文章 目的是构建我们的Article对象
         * 2.作者id 当前的登录用户,从线程池获取
         * 3.标签 要将标签加入我们的关联列表当中
         * 4.body 内容存储article bodyId
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
//        插入之后 会生成一个文章id
        articleMapper.insert(article);


        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }

        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);



        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);

//        Map<String,String> map = new HashMap<>();
//        map.put("id",article.getId().toString());
//        return Result.success(map);

    }


    //复制每一篇文章
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList=new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }


    //复制每一篇文章
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList=new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }




    @Autowired(required = false)
    private CategoryServiceImpl categoryService;


    //复制一篇文章的属性
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口都需要标签，作者信息
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }

        if(isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }

        return articleVo;
    }




    @Autowired(required = false)
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


}
