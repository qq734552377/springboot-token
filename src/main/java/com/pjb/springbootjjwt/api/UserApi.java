package com.pjb.springbootjjwt.api;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pjb.springbootjjwt.annotation.PassToken;
import com.pjb.springbootjjwt.annotation.UserLoginToken;
import com.pjb.springbootjjwt.entity.BaseResult;
import com.pjb.springbootjjwt.entity.RequestState;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.exceptions.AthException;
import com.pjb.springbootjjwt.service.TokenService;
import com.pjb.springbootjjwt.service.UserService;
import com.pjb.springbootjjwt.tools.JWTTool;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author jinbin
 * @date 2018-07-08 20:45
 */
@RestController
@RequestMapping("api")
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    //登录
    @PostMapping("/login")
    public Object login(@RequestBody User user, @RequestHeader("user-agent") String user_agent){
        JSONObject jsonObject=new JSONObject();
        User userForBase=userService.findByUsername(user);
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在");
            return jsonObject;
        }else {
            if (!userForBase.getPassword().equals(user.getPassword())){
                jsonObject.put("message","登录失败,密码错误");
                return jsonObject;
            }else {
                String token = tokenService.getToken(userForBase);
                String tk = JWTTool.createJWT(10*60*1000,userForBase);
                jsonObject.put("token", token);
                //todo 可以将token存入缓存中  以方便作为唯一token认证  如果缓存中没有token则存入 如果有则替换
                try{
//                    Thread.sleep(10*1000);
                }catch (Exception e){

                }

                jsonObject.put("user", userForBase);
                jsonObject.put("user2", tk);
                return jsonObject;
            }
        }
    }
    @UserLoginToken
    @GetMapping("/getMessage")
    public BaseResult getMessage(@RequestHeader String token,@RequestHeader String tk) throws AthException{
        BaseResult baseResult = new BaseResult();
        DecodedJWT f= JWT.decode(tk);
        Map<String, Claim> claims = f.getClaims();
        String id = f.getClaim("id").asString();
        User user = userService.findUserById(id);
        if(JWTTool.isVerify(tk,user)){
            baseResult.setStatus(RequestState.SUCCESS);
            baseResult.setInfo("你已通过验证");
        }else {
            baseResult.setStatus(RequestState.FAIL);
            baseResult.setInfo("无效验证，请重新登录");
        }
        return baseResult;
    }

    @GetMapping("test/{test}")
    public String testNoAth(@PathVariable String test){
        return test;
    }
}
