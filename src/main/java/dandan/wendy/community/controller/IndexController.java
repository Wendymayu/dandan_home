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
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "search", required = false) String search){


        PaginationDTO pagination = questionService.List(search,page,size);
        //页面通过该处的questions 才能获得model中响应的内容
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        return "index";
    }
}
