package com.liu.service;

import com.liu.pojo.Comment;

import java.util.List;

/**
 * @author tianse
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
