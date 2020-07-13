package dandan.wendy.community.mapper;

import dandan.wendy.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
   //插入一个用户,在xml中实现
   @Insert("insert into user(name,accountId,token,gmtCreate,gmtModified,avatarUrl)values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})" )
   public void insert(User user);

   @Select("select * from user")
   public List<User> findAll();

   @Select("select * from user where token = #{token}")
   User findByToken(@Param("token") String token);

   /*使用该方法可以给数据库添加一列bio,必须指定varchar(20)*/
   /*@Insert("alter table user add column bio varchar(200)")*/
   @Insert("alter table user add avatarUrl varchar(100) null")
   public void addColumn();

   @Select("select * from user where id=#{id}")
    User findById(@Param("id")Integer  id);

   @Select("select * from user where accountId=#{accountId}")
    User fingByAccountId(@Param("accountId")String accountId);

   @Update("update user set name = #{name},token = #{token},gmtModified = #{gmtModified},avatarUrl = #{avatarUrl} where id =#{id}")
   void update(User user);
}

