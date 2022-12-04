package com.huang.BBS.controller;

import com.huang.BBS.common.aop.LogAnnotation;
import com.huang.BBS.service.ArticleService;
import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.params.ArticleParam;
import com.huang.BBS.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据进行交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */

    //加上此注解 代表对此接口记录日志
    @LogAnnotation(module="文章",operation="获取文章列表")
    @PostMapping
    public Result articles(@RequestBody PageParams pageParams) {
        //ArticleVo 页面接收的数据
        return articleService.listArticle(pageParams);
    }


    /**
     * 首页，最热文章
     * @return
     */
    @PostMapping("hot")
    public Result hotarticles() {
        int limit = 5;
        return articleService.hotarticles(limit);
    }


    /**
     * 首页，最新文章
     * @return
     */
    @PostMapping("new")
    public Result newarticles() {
        int limit = 5;
        return articleService.newarticles(limit);
    }



    /**
     * 首页，文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }





    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id) {

        return articleService.findArticleById(id);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }


}