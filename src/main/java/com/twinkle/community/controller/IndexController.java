package com.twinkle.community.controller;

import com.twinkle.community.dto.PagenationDTO;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.User;
import com.twinkle.community.model.UserExample;
import com.twinkle.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name ="size",defaultValue = "5") Integer size
                        ){
       checkUser(request);

        /*分页相关*/
        PagenationDTO pagenation = questionService.getQuestionList(page,size);
        model.addAttribute("pagenation",pagenation);

        return "index";
    }

    public void checkUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        //通过查看cookie是否为null来判断缓存中是否有用户信息,如果有就直接获取并返回到页面进行渲染
        if (cookies != null){
//        System.out.println("cookies length ---"+cookies.length);
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
//
                    //System.out.printf("index-user==="+userExample.createCriteria().andTokenEqualTo(token));
                    userExample.createCriteria().andTokenEqualTo(token);

                    List<User> users  = userMapper.selectByExample(userExample);

                    if (users.size()!=0){
                        request.getSession().setAttribute("user",users.get(0));
                    }
                    break;
                }
            }
        }else{
            System.out.println("cookies中没有缓存");
        }
    }
}
