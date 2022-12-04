package com.huang.BBS.controller;

import com.huang.BBS.service.CommentsService;
import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

//    /comments/create/change

    @Autowired(required = false)
    private CommentsService commentsService;


    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticleId(id);
    }


    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
