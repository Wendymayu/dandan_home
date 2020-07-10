package dandan.wendy.community.testUserMapper;

import dandan.wendy.community.CommunityApplication;
import dandan.wendy.community.mapper.UserMapper;
import dandan.wendy.community.model.User;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommunityApplication.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void findAll(){
        List<User> users = userMapper.findAll();
        System.out.println(users.get(0).getName());
    }

    @Test
    public void addColumn(){
        userMapper.addColumn();
    }
}
