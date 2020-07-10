package dandan.wendy.community.mapper;

import dandan.wendy.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmtCreate,gmtModefied,creator,tag) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModefied},#{creator},#{tag})")
    public void create(Question question);
}
