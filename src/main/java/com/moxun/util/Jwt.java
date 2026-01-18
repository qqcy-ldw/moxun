package com.moxun.util;

import com.moxun.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
public class Jwt {

    @Autowired
    private JwtConfig jwtConfig;

    private static String SECRET_KEY;
    private static long EXPIRATION_TIME;

    @PostConstruct
    public void init() {
        SECRET_KEY = jwtConfig.getSECRET_KEY();
        EXPIRATION_TIME = jwtConfig.getEXPIRATION_TIME();
    }
    // 生成密钥（使用 HMAC-SHA256 算法，需要 256 位密钥）
    private static Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成登录令牌
     * @param claims
     * @return
     */
    public static String generateToken(HashMap<String, Object> claims) {

        // 生成令牌：头部（Header）默认包含算法，载荷包含自定义信息+过期时间，签名用密钥加密
        return Jwts.builder()
                .setClaims(claims) // 设置自定义载荷
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 签名算法+密钥
                .compact(); // 压缩为字符串
    }


    /**
     * 验证令牌有效性（签名正确+未过期）
     * @param token 待验证的令牌
     * @return 验证通过返回 true，否则 false（或抛出异常）
     */
    public static Claims validateToken(String token) {
            // 解析令牌并验证签名和过期时间
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 用相同密钥验证签名
                .build()
                .parseClaimsJws(token);// 解析令牌（自动校验过期时间）
        log.info("jws:{}",jws);
        log.info("jws.getBody():{}",jws.getBody());
        return jws.getBody();
    }
}
