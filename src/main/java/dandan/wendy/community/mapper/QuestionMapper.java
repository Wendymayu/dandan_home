package dandan.wendy.community.mapper;

import dandan.wendy.community.dto.QuestionDTO;
import dandan.wendy.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmtCreate,gmtModified,creator,tag) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> List(@Param(value = "offset") int offset, @Param(value="size") int size);

    @Select("select count(1) from question;")
    public int count();

    //开始我这个方法也叫List<会报错，Mapper中的方法还是不要重名。
    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> ListByUserId(@Param("userId")Integer userId, @Param(value = "offset") int offset, @Param(value="size") int size);

    @Select("select count(1) from question where creator = #{userId};")
    int countByUserId(@Param(value = "userId")Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id")Integer id);

    @Update("update question set title = #{title},description = #{description},gmtModified = #{gmtModified},tag = #{tag} where id =#{id}")
    void update(Question question);
}
