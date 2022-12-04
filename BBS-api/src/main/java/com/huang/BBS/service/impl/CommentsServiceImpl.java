package com.huang.BBS.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huang.BBS.dao.mapper.CommentMapper;
import com.huang.BBS.dao.pojo.Comment;
import com.huang.BBS.dao.pojo.SysUser;
import com.huang.BBS.service.CommentsService;
import com.huang.BBS.service.SysUserService;
import com.huang.BBS.utils.UserThreadLocal;
import com.huang.BBS.vo.CommentVo;
import com.huang.BBS.vo.Result;
import com.huang.BBS.vo.UserVo;
import com.huang.BBS.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired(required = false)
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章id 查询 评论列表 从comments表中查询
         * 2.根据作者的id查询作者的信息
         * 3.判断如果lever=1，要去查询他有没有子评论
         * 4.如果有，根据评论id查询（parent_id）
         */

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList =copyList(comments);
        return Result.success(commentVoList);
    }



    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);


    }





    public List<CommentVo> copyList(List<Comment> commentList){
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo=new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //作者信息
        Long articleId = comment.getArticleId();

        UserVo userVo = sysUserService.findUserVoById(articleId);
        commentVo.setAuthor(userVo);

        //子评论

        Integer level = comment.getLevel();
        if(1==level){
            Long id =comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //to user 给谁评论
        if(level>1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper .eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
