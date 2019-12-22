package com.twinkle.community.controller;

import com.twinkle.community.mapper.QuestionMapper;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.Question;
import com.twinkle.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletRegistration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;
    
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String dopublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            HttpServletRequest request, Model model){
        //回显数据
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        //基本校验
        if (title ==null || title.equals("")){
            model.addAttribute("err","标题不能为空");
            return "publish";
        }
        if (description == null || title.equals("")){
            model.addAttribute("err","内容不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")){
            model.addAttribute("err","标签不能为空");
            return "publish";
        }

        //question表需要写入谁发布的这篇文章,需要userid,如果用户已登录过,浏览器存在token,就去数据库查询用户
        //获取用户
        User user =null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        if(user==null){
            model.addAttribute("err","用户未登录");
            return "publish";
        }
        //question-set
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtUpdate(question.getGmtCreate());
        questionMapper.createQuestion(question);
        return "redirect:/";
    }
}
