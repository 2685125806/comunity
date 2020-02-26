package com.twinkle.community.controller;

import com.twinkle.community.dto.QuestionDTO;
import com.twinkle.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *问题详情页面
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String questionDetail(@PathVariable("id") Integer id,
                                 Model model){
        //1.查询question详情
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        model.addAttribute("questionDTO",questionDTO);
        //2.查询作者名称
        //3.赋值给questionDTO
        //4.发送到question视图进行渲染
        return "question";
    }
    public void questionLike(){
//        1.更新数据库
//        2.回显页面
    }

}
