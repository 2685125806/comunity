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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;


    /*我的问题-请求*/
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")  String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){
//        System.out.println("PC---action--"+action);
        Cookie[] cookies = request.getCookies();
        User user = null;
        /*查看缓存是否存有user信息,有就根据token去数据库查询user并返回到页面渲染*/
        if (cookies != null){
//        System.out.println("cookies length ---"+cookies.length);
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())&&cookie.getValue()!=""){
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);

                    if (users.size()!=0){
                        user = users.get(0);
                        request.getSession().setAttribute("user",user);

                        System.out.println("PC1--"+user.getAvatarUrl());
                        System.out.println("PC1--"+user.getId());
                    }
                    break;
                }
            }
            /*如果传入的参数action中的值是questions/replies就将页面小标题(首页是发现)改为对应项并进行渲染*/
            if ("questions".equals(action)){
                model.addAttribute("section","questions");
                model.addAttribute("sectionName","我的提问");

            }else if ("replies".equals(action)){
                model.addAttribute("section","replies");
                model.addAttribute("sectionName","最新回复");
            }
            System.out.println("PC-1--"+user.getId());
            /*这里做的是'我的问题'的请求,需要根据我的id查询问题列表并送到页面进行渲染,然后跳转到profile*/
            PagenationDTO pagenationDTO = questionService.questionList(user.getId(),page,size);
            System.out.println("PC--2-");
            model.addAttribute("pagenation",pagenationDTO);

            return "profile";
        }
        else {
          //m没有登录,提示登录
            model.addAttribute("err","请先登录");
            return "index";
        }
    }

    @GetMapping("/profile/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().invalidate();      //清session
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            System.out.println(cookie.getValue());
        }
//        response.addCookie(new Cookie("token",""));
//        System.out.println("logout---");
        return "redirect:/";
    }

}
