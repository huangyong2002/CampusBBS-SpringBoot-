package com.huang.BBS.controller;


import com.huang.BBS.service.ArticleService;
import com.huang.BBS.service.TagService;
import com.huang.BBS.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired(required = false)
    private TagService tagService;
    @Autowired(required = false)
    private ArticleService articleService;



    //  /tags/hot
    @GetMapping("hot")
    public Result hot(){
      int limit =6;
      return tagService.hots(limit);
    }



    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }




    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }


    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }

}
