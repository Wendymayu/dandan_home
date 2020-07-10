package dandan.wendy.community.mapper;

import dandan.wendy.community.model.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    //@Select("select * from account")
    public List<Student> findAll();

    public void add(Student stu);
}
