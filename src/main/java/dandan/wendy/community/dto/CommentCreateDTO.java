package dandan.wendy.community.dto;

import dandan.wendy.community.model.User;
import lombok.Data;

/**
 * Created by codedrinker on 2019/6/2.
 */
@Data
public class CommentCreateDTO {
    //private Long id;
    private Long parentId;
    private Integer commentType;
    private Long commentator;
    private String content;

    /*public void setUser(User user) {
    }*/

}
