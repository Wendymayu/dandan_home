package dandan.wendy.community.mapper;


import dandan.wendy.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}