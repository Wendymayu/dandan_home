package dandan.wendy.community.controller;
import dandan.wendy.community.dto.PaginationDTO;
import dandan.wendy.community.mapper.UserMapper;
import dandan.wendy.community.model.User;
import dandan.wendy.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by codedrinker on 2019/5/15.
 */
@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

   // @Autowired
  //  private NotificationService notificationService;

   @Autowired
   private UserMapper userMapper;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

       /* User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }*/

       User user = null;
        Cookie[] cookies = request.getCookies();
        System.out.println("cookies是 "+cookies);

        if(cookies !=null || cookies.length !=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
           /* PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);*/
        } else if ("replies".equals(action)) {
           // PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
           // model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
        }
        int userId = user.getId();
       // PaginationDTO pagination = questionService.List(user.getId(),page,size);
        PaginationDTO pagination = questionService.List(12,page,size);
        model.addAttribute("pagination",pagination);
        return "profile";
    }
}
