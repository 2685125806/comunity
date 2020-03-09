package com.twinkle.community.controller;

import com.twinkle.community.dto.AccessTokenDTO;
import com.twinkle.community.dto.GithubUser;
import com.twinkle.community.mapper.UserMapper;
import com.twinkle.community.model.User;
import com.twinkle.community.model.UserExample;
import com.twinkle.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecert;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                         @RequestParam(name = "state") String state,
                         Model model,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecert);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);


        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        System.out.println("accessToken--"+accessToken);
        GithubUser githubUser = gitHubProvider.getUser(accessToken);
        System.out.println("AC-callback---"+githubUser);
        if(githubUser != null){
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(githubUser.getId());
            List<User> users = userMapper.selectByExample(userExample);
            System.out.println("ac---uses0--"+users.get(0));
            if (users != null){
                //如果根据AccountId能够查询出user,说明数据库中以存在此用户
                String token = users.get(0).getToken();//直接从数据库查询并且写入session中
                System.out.println("token---"+token);
                response.addCookie(new Cookie("token",token));
                //登录成功,写cookie和session
                request.getSession().setAttribute("user",users.get(0));
            }else{
//                /否则进行插入操作
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setName(githubUser.getName());
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                user.setAvatarUrl(githubUser.getAvatar_url());
//            userMapper.insertUser(user);
                userMapper.insert(user);
            }
            return "redirect:/";
        }else{
            //登录失败重新登录
          //  model.addAttribute("err","登录失败,请稍后再试");
            return "redirect:/";
        }


    }
}
