package com.twinkle.community.controller;

import com.twinkle.community.dto.CommentDTO;
import com.twinkle.community.mapper.CommentMapper;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.Comment;
import com.twinkle.community.model.User;
import com.twinkle.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IndexController indexController;

    @PostMapping(value = "/Comment")
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request, Model model) {
        List<User> users = null;
        Cookie[] cookies = request.getCookies();
        //通过查看cookie是否为null来判断缓存中是否有用户信息,如果有就直接获取并返回到页面进行渲染
        if (cookies == null) {
            model.addAttribute("message", "亲,请先登录在评论!");
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    users = userMapper.selectByExample(userExample);
                    break;
                }
//
                Comment comment = new Comment();
                comment.setParentId(commentDTO.getParentId());
                comment.setType(commentDTO.getType());
                comment.setContent(commentDTO.getContent());
                comment.setGmtCreate(System.currentTimeMillis());
                comment.setGmtUpdate(System.currentTimeMillis());
                comment.setCommentor(users.get(0).getId());
                comment.setLikecount(0L);
                commentMapper.insert(comment);

            }
        }
        return null;
    }
}