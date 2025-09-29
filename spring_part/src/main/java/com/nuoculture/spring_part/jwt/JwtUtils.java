package com.nuoculture.spring_part.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

// jwt工具类
@Component  // 注入spring容器
public class JwtUtils {
    // 签发者
    private static String issuer = "cyy";
    // 密钥
    private static String secret = "cyy999";
    // 过期时间 30分钟
    private static String expiration = "30";

    // 根据用户信息，生成token
    public static String createToken(String userId, String nickName) {
        //设置令牌的过期时间
        Calendar calendar = Calendar.getInstance();
        //设置过期时间
        calendar.add(Calendar.MINUTE, Integer.parseInt(expiration));
        //创建jwt构建对象
        JwtBuilder builder = Jwts.builder();
        //创建加密对象
        SecretKey secretKey = createSecret();
        //获取 Token
        String token = builder.setId(userId) // 设置id
                .setIssuer(issuer) // 设置签发者
                .setSubject(nickName) // 设置主题
                .setExpiration(new Date(Long.MAX_VALUE)) // 设置过期时间
//                .setExpiration(calendar.getTime()) // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, secretKey) // 设置加密算法
                .compact(); // 获取token
        return token;
    }

    //生成加密的密钥对象
    public static SecretKey createSecret() {
        byte[] decode = Base64.getDecoder().decode(secret);
        //创建加密对象
        SecretKeySpec key = new SecretKeySpec(decode, 0, decode.length, "AES");
        return key;
    }

    //解析
    public static Claims parseToken(String token) {
        //创建加密对象
        SecretKey secretKey = createSecret();
        //解析token
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims;
    }

    //判断token
    public static boolean checkToken(String token) {
        //判断token
        if(token == null){
            return false;
        }
        //创建加密对象
        SecretKey secretKey = createSecret();
        try {
            //解析token
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
          //  e.printStackTrace();
            return false;
        }
    }


  /*  public static void main(String[] args) {
        JwtUtils jwtUtils = new JwtUtils();
        String token = jwtUtils.createToken("1", "magic");
        System.out.println(token);
        String subject = jwtUtils.parseToken(token);
       System.out.println(subject);
    } */
}
