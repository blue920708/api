package com.choi.api.core.security.service;

import com.choi.api.biz.user.model.User;
import com.choi.api.biz.user.model.UserToken;
import com.choi.api.core.exception.BizException;
import com.choi.api.core.exception.UnauthorizedException;
import com.choi.api.core.util.RequestUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String create(String subject, String key, String data, int expireTime){

        Map<String, Object> claims = new HashMap<>();
        claims.put(key, data);

        String jwt = Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return jwt;

    }

    public String getAccessToken(){
        String AccessToken = RequestUtils.getRequestHeader("Authorization");
        if (StringUtils.isEmpty(AccessToken) || !AccessToken.startsWith("Bearer ")) {
            return null;
        }
        return AccessToken.substring(7);
    }

    public String get(String accessToken, String key){
        String claims = (String) Jwts.parser().setSigningKey(secret).parseClaimsJws(accessToken).getBody().get(key);

        return claims;
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }


    public UserToken refresh(String refreshToken, User user) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken).getBody();
        // 리프레시 토큰인지 확인
        if (!"refresh".equals(claims.getSubject())) {
            log.debug("JwtUtils 디버그 -> 갱신 토큰 : {} ", refreshToken);
            log.debug("JwtUtils 디버그 -> 갱신 토큰이 아닙니다");
            throw new BizException("잘못된 접근입니다. 다시 로그인해주세요.");
        }

        // 유저네임 일치여부
        if (user.getId().equals(claims.get("id"))) {
            log.debug("JwtUtils 디버그 -> 갱신 토큰 : {} ", refreshToken);
            log.debug("JwtUtils 디버그 -> 사용자 계정 불일치");
            throw new BizException("잘못된 접근입니다. 다시 로그인해주세요.");
        }

        // 토큰갱신
        if (!isTokenExpired(refreshToken)) {
            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getId());
            String accessToken = create(user.getId(), "id", user.getId(), 1000 * 60 * 5);
            refreshToken = create(user.getId(), "id", user.getId(), 1000 * 60 * 60 * 30);
            UserToken userToken = UserToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            log.debug("JwtUtils 디버그 -> 새로운 유저 토큰 : {} ", userToken);
        }

        return null;
    }
}