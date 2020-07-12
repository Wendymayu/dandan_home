package dandan.wendy.community.controller;

import dandan.wendy.community.dto.PaginationDTO;
import dandan.wendy.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "1") int page,
                        @RequestParam(name="size",defaultValue = "5") int size){

        //没用拦截器的登录写法
        /*Cookie[] cookies = request.getCookies();
        if(cookies !=null || cookies.length !=0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }*/

        PaginationDTO pagination = questionService.List(page,size);
        //页面通过该处的questions 才能获得model中响应的内容
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
