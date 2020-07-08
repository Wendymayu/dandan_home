package dandan.wendy.community.controller;

import dandan.wendy.community.dto.AccessTokenDTO;
import dandan.wendy.community.dto.GithubUser;
import dandan.wendy.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code, @RequestParam(name="state") String state, HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //System.out.println(accessToken.toString());
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null){
            //登录成功
            request.getSession().setAttribute("githubUser",githubUser);
            return  "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }
}
