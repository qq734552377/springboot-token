package com.pjb.springbootjjwt.tools;

import com.auth0.jwt.JWT;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.exceptions.AthException;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

/**
 * Created by pj on 2018/12/28.
 */
public class JWTTool {
    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法  私匙使用用户密码
     *
     * @param ttlMillis jwt过期时间
     * @param user      登录成功的user对象
     * @return
     */
    public static String createJWT(long ttlMillis, User user) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String key = getEncodePwd(user.getPassword(),Integer.parseInt(user.getId()));
        claims.put("password", key);

        //生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。


        //生成签发人
        String subject = "这是私有声明";



        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(subject)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /**
     * Token的解密
     * @param token 加密后的token
     * @param user  用户的对象
     * @return
     */
    public static Claims parseJWT(String token, User user) throws AthException{
        Claims claims = null;
        try {
            //签名秘钥，和生成的签名的秘钥一模一样
            String key = JWT.decode(token).getClaim("password").asString();

            //得到DefaultJwtParser
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(key)
                    //设置需要解析的jwt
                    .parseClaimsJws(token).getBody();
        }catch (Exception e){
            throw new AthException(e.toString());
        }
        return claims;
    }


    /**
     * 校验token
     * 在这里可以使用官方的校验，我这里校验的是token中携带的密码于数据库一致的话就校验通过
     * @param token
     * @param user
     * @return
     */
    public static Boolean isVerify(String token, User user) throws AthException{

        Claims claims = parseJWT(token,user);
        String base64Str = (String) claims.get("password");
        int id = Integer.parseInt(claims.get("id").toString());
        if (getDecodePwd(base64Str,id).equals(user.getPassword())) {
            return true;
        }

        return false;
    }


    public static String getEncodePwd(String pwd, int userid){
        byte[] bytes = pwd.getBytes();
        byte[] br = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            br[i] = (byte) (userid ^ bytes[i]) ;
        }
        return Base64.encode(br,Base64.BASE64DEFAULTLENGTH);
    }

    public static String getDecodePwd(String pwdBase64,int id){

        byte[] br = null;
        try {
             br = Base64.decode(pwdBase64);
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        byte[] pwd = new byte[br.length];
        for (int i = 0; i < br.length; i++) {
            pwd[i] = (byte)(br[i] ^ id);
        }
        return new String(pwd);
    }

    public static String getStringByYH(String str){
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i]);
        }
        return new String(bytes);
    }
}
