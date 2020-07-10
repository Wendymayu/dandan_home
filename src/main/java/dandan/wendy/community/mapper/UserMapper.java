package dandan.wendy.community.mapper;

import dandan.wendy.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
   public void insert(User user);

   @Select("select * from user")
   public List<User> findAll();

   @Select("select * from user where token = #{token}")
   User findByToken(@Param("token") String token);
   /*使用该方法可以给数据库添加一列bio,必须指定varchar(20)*/
   @Insert("alter table user add column bio varchar(200)")
   public void addColumn();

}

