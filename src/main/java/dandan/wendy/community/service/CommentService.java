package dandan.wendy.community.service;

import dandan.wendy.community.dto.CommentDTO;
import dandan.wendy.community.enums.CommentTypeEnum;
import dandan.wendy.community.enums.NotificationStatusEnum;
import dandan.wendy.community.enums.NotificationTypeEnum;
import dandan.wendy.community.exception.CustomizeErrorCode;
import dandan.wendy.community.exception.CustomizeException;
import dandan.wendy.community.mapper.*;
import dandan.wendy.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by codedrinker on 2019/5/31.
 */
@Service
public class CommentService {


    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

   @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getCommentType() == null || !CommentTypeEnum.isExist(comment.getCommentType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }



        if (comment.getCommentType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            //回复的是哪个问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //增加二级评论数
            commentMapper.insert(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);


            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());

        } else {
            // 回复问题，在question表中看评论数
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            //评论的插入和问题的评论数要么都操作，要么都不操作
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            createNotify(comment,question.getCreator(),commentator.getName(), question.getTitle(),NotificationTypeEnum.REPLY_QUESTION, question.getId());

        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if(receiver==comment.getCommentator()){
            return;
        }

       Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotiType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setNotiStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);

        //上面方法体可由下面变化而来
       /* Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotiType(NotificationTypeEnum.REPLY_COMMENT.getType());
        notification.setOuterId(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        notification.setNotiStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(dbComment.getCommentator());
        notificationMapper.insert(notification);*/
    }


    //因为评论即用于问题，又用于一级评论，把方法名由listByQuestionId改为listByTargetId
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andCommentTypeEqualTo(type.getType());

        //评论按创建时间倒序排列
        commentExample.setOrderByClause("gmtCreate desc");

        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        // 获取评论人并转换为 Map，容易查找
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        System.out.println("users   "+users);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;

    }



    /*private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == comment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }*/

}
