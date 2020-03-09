package com.twinkle.community.controller;

import com.twinkle.community.mapper.QuestionMapper;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.Question;
import com.twinkle.community.model.User;
import com.twinkle.community.model.UserExample;
import com.twinkle.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        Question question = questionMapper.selectByPrimaryKey(id);
        model.addAttribute("title",question.getTitle());
        System.out.println("PC---"+question.getDescription());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",id);

        return "publish";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String dopublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Integer id,
            HttpServletRequest request, Model model){

//        Cookie[] cookies = request.getCookies();
//        if ()


        System.out.println("编辑--id--"+id);
        //回显数据
        model.addAttribute("title",title);
//        model.addAttribute("description",description);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("id",id);
        //基本校验
        if (title ==null || title.equals("")){
            model.addAttribute("err","标题不能为空");
            return "publish";
        }
        if (title.length()>40){
            model.addAttribute("err","标题长度不能超过40个字");
            return "publish";
        }
        if (description == null || description.equals("")){
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
        if (cookies != null){ //cookies不为空,即登录状态
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                   UserExample userExample = new UserExample();
                   userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size()!=0){
                        user = users.get(0);
                        request.getSession().setAttribute("user",users.get(0));
                    }
                    break;
                }
            }
            //question-set
            Question question = new Question();
            question.setTitle(title);
            question.setDescription(description);
            question.setTag(tag);
            question.setCreator(user.getId());
            question.setId(id);
            questionService.createOrUpdate(question);

            return "redirect:/";

        }
        else{
            model.addAttribute("err","用户未登录,请先登录");
            return "publish";
        }
    }
}
