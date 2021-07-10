package com.liu.service;

import com.liu.dao.CommentRepository;
import com.liu.pojo.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tianse
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);

    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
       Long parentCommentId = comment.getParentComment().getId();
       if(parentCommentId != -1){
           comment.setParentComment(commentRepository.getOne(parentCommentId));
       }else{
           comment.setParentComment(null);
       }
        comment.setCreateTime(new Date());

        return commentRepository.save(comment);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     *
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        //遍历评论把数据放到新的集合
        for (Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的个层子代到第一季子代集合
        combineChildren(commentsView);
        return commentsView;
    }
    /**
     * @param  comments root节点，blog不为空的对象集合
     */
    private void combineChildren(List<Comment> comments){
        for (Comment comment : comments){
            List<Comment> replys1 = comment.getReplyComment();
            for (Comment reply1 : replys1){
                //循环迭代，找出子代，存放在temRepls中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComment(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }
    //存放迭代找出所有的自子代集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     *
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶级节点添加到临时存放集合
        if(comment.getReplyComment().size()>0){
            List<Comment> replys = comment.getReplyComment();
            for (Comment reply : replys){
                tempReplys.add(reply);
                if(reply.getReplyComment().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}

