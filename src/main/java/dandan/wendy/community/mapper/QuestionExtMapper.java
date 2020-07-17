package dandan.wendy.community.mapper;


import dandan.wendy.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);

    public int incCommentCount(Question question);

    List<Question> selectRelated(Question question);
}