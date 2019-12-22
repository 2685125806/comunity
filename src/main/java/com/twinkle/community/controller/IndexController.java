package com.twinkle.community.controller;

import com.twinkle.community.dto.QuestionDTO;
import com.twinkle.community.mapper.QuestionMapper;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.Question;
import com.twinkle.community.model.User;
import com.twinkle.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
//        System.out.println("cookies length ---"+cookies.length);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        }

        List<QuestionDTO> questionList = questionService.getQuestionList();
        model.addAttribute("questions",questionList);
        return "index";
    }
}
