package dandan.wendy.community.controller;

import dandan.wendy.community.dto.QuestionDTO;
import dandan.wendy.community.mapper.QuestionMapper;
import dandan.wendy.community.model.Question;
import dandan.wendy.community.model.User;
import dandan.wendy.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    //编辑已发布问题的方法
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") int id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        return "publish";
    }
    
    @GetMapping("/publish")
    public String publish(){
        System.out.println("________________");
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "id", required = false) Integer id,
                            HttpServletRequest request,
                            Model model) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("id", id);

        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        System.out.println("能得到user吗");
        User user = (User) request.getSession().getAttribute("user");
        System.out.println("能，user是  "+user);
       /* User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null || cookies.length != 0){
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
    }*/
        //空指针下面测试输出会报 空指针异常
        //System.out.println(user.getName());
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());

        //注意此处，如果id为空，说明我们新增问题；Id不为空，说明我们更新问题
        //在create方法中同样要这样进行判断
        if(id!=null){
            question.setId(id);
        }else{

        }

        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
