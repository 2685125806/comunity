package com.twinkle.community.provider;

import com.alibaba.fastjson.JSON;
import com.twinkle.community.dto.AccessTokenDTO;
import com.twinkle.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {
    /**
     * 获取accessToken 下边整个方法都是github提供的,应该是
     * @param accessTokenDTO
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                String token = string.split("&")[0].split("=")[1];
                return token;
        } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * 再根据accessToken发送请求查询user信息,返回json字符串
     * @param accessToken
     * @return
     */
    public GithubUser getUser(String accessToken){
        System.out.println("GP---"+accessToken);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        try {
            System.out.println("GP---getUser-request--"+request);
            Response response = client.newCall(request).execute();
            System.out.println("respose--"+response);
            String string = response.body().string();
            System.out.println("string--"+string+"---accessToken---"+accessToken);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            System.out.println("GP--"+githubUser);
            return githubUser;
        } catch (IOException e) {
            System.out.println("getGitUser出错了");
           e.printStackTrace();
        }
        return  null;
    }
}
