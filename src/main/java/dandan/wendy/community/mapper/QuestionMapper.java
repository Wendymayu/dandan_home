package dandan.wendy.community.mapper;

import dandan.wendy.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    List<Question> ListByUserId(@Param("userId")int userId, @Param(value = "offset") int offset, @Param(value="size") int size);

    @Select("select count(1) from question where creator = #{userId};")
    int countByUserId(@Param(value = "userId")int userId);
}
