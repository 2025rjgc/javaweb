package com.example.demo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

public class JWTUtils {

    // 密钥
    private static final String SECRET_KEY = "cmVhZGluZ19leGNoYW5nZV9wbGF0Zm9ybQ==";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * 生成JWT令牌
     *
     * @param id        JWT的唯一标识符（用户ID）
     * @param subject   主题信息（用户名和密码）
     * @param ttlMillis 过期时间（毫秒）
     * @return 返回生成的JWT字符串
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        // 当前时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 设置过期时间
        Date expirationDate = new Date(nowMillis + ttlMillis);

        // 构建JWT
        return Jwts.builder()
                .setId(id) // JWT ID
                .setSubject(subject) // 主题
                .setIssuedAt(now) // 签发时间
                .setExpiration(expirationDate) // 过期时间
                .signWith(KEY) // 使用密钥签名
                .compact();
    }

    /**
     * 验证JWT令牌并解析其内容
     *
     * @param jwtStr JWT字符串
     * @return 如果验证成功，返回JWT的主题信息（subject）
     * @throws JwtException 如果JWT无效或已过期
     */
    public static Claims verifyJWT(String jwtStr) throws JwtException {
        if (StringUtils.hasLength(jwtStr)) {
            throw new IllegalArgumentException("JWT字符串不能为空");
        }

        try {
            // 解析JWT
            // 使用相同的密钥进行验证
            // 解析JWT
            // 返回主题信息
            return Jwts.parserBuilder()
                    .setSigningKey(KEY) // 使用相同的密钥进行验证
                    .build()
                    .parseClaimsJws(jwtStr) // 解析JWT
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT已过期", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("JWT格式错误", e);
        } catch (@SuppressWarnings("deprecation") SignatureException e) {
            throw new JwtException("JWT签名验证失败", e);
        } catch (Exception e) {
            throw new JwtException("JWT验证失败", e);
        }
    }
}