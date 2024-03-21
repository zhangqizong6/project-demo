package com.config.utils.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class TokenUtil {
    private static String key = "1NQVE5guDI1iv0ZOYzODH6L8WX5cAWQ";

    public static String getJWTString(String username, String role, Date expires) {
        if (username == null) {
            throw new NullPointerException("null username is illegal");
        }

        if (expires == null) {
            throw new NullPointerException("null expires is illegal");
        }
        if (key == null) {
            throw new NullPointerException("null key is illegal");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String jwtString = Jwts.builder().setIssuer("Jersey-Security-Basic").setSubject(username).setAudience(role)
                .setExpiration(expires).setIssuedAt(new Date()).setId("1").signWith(signatureAlgorithm, key).compact();
        return jwtString;
    }

    public static boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
            return true;
        } catch (Exception e) {
            log.error("无效的token：{}",token);
            return false;
        }
    }

    public static String getName(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getSubject();
        }
        return null;
    }

    public static String getRole(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return claimsJws.getBody().getAudience();
        }
        return new String();
    }

    public static int getVersion(String jwsToken) {
        if (isValid(jwsToken)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            return Integer.parseInt(claimsJws.getBody().getId());
        }
        return -1;
    }

    public static Date getExpiryDate(int minutes) {

        // 根据当前日期，来得到到期日期
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);

        return calendar.getTime();
    }


    private static String userkey = "1NQVE5guDI1iv0ZOYzODH6L8WX5cAWQ";
    public static String getUserJWTString(String userId, String role, Date expires) {
        if (userId == null) {
            throw new NullPointerException("null username is illegal");
        }
        if (expires == null) {
            throw new NullPointerException("null expires is illegal");
        }
        if (key == null) {
            throw new NullPointerException("null key is illegal");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String jwtString = Jwts.builder().setIssuer("Jersey-Security-Basic").setSubject(userId).setAudience(role)
                .setExpiration(expires).setIssuedAt(new Date()).setId("1").signWith(signatureAlgorithm, userkey).compact();
        return jwtString;
    }


    public static boolean userIsValid(String token,String userId) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(userkey).parseClaimsJws(token.trim());
            Claims body = claimsJws.getBody();
            String subject = body.getSubject();
            if (StringUtils.isEmpty(subject) || StringUtils.isEmpty(userId)){
                log.error("用户登录小程序无效的token：{}",token);
                return false;
            }
            if (subject.equals(userId)){
                return true;
            }
            log.error("用户登录id与token解密id不符：{}，登录id：{}，token解密id：{}",token,userId,subject);
            return false;
        } catch (Exception e) {
            log.error("用户登录小程序无效的token：{}",token);
            return false;
        }
    }

}