package dandan.wendy.community.controller;

import dandan.wendy.community.dto.CommentCreateDTO;
import dandan.wendy.community.dto.CommentDTO;
import dandan.wendy.community.dto.ResultDTO;
import dandan.wendy.community.enums.CommentTypeEnum;
import dandan.wendy.community.exception.CustomizeErrorCode;
import dandan.wendy.community.model.Comment;
import dandan.wendy.community.model.User;
import dandan.wendy.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by codedrinker on 2019/5/30.
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method= RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        System.out.println("user是  "+user);
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }


        //if (commentCreateDTO == null || commentCreateDTO.getContent()== null ||commentCreateDTO.getContent()=="") {
        //apache的lang3包里的StringUtils可以判断字符串是否存在或者是否为空
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setCommentType(commentCreateDTO.getCommentType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment,user);

        return ResultDTO.okOf();

    }


    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }

}
