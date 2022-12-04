package com.huang.BBS.service;

import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.params.ArticleParam;
import com.huang.BBS.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);





    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotarticles(int limit);


    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newarticles(int limit);


    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 文章详情
     * @param id
     * @return
     */
    Result findArticleById(Long id);


    /**
     * 文章发布服务
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}
