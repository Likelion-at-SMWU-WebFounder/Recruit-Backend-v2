package com.smlikelion.webfounder.security;

import com.smlikelion.webfounder.admin.entity.Role;
import com.smlikelion.webfounder.security.exception.ExpiredTokenException;
import com.smlikelion.webfounder.security.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
    private final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 30; // 30일

    private final String BEARER_TYPE = "Bearer ";

    @Value("${secret.jwt}")
    private String baseSecretKey;

    //토큰 생성
    public TokenInfo createAccessToken(String accountId, Role role) {
        return createToken(accountId, role, ACCESS_TOKEN_VALID_TIME);
    }

    public TokenInfo createRefreshToken(String accountId, Role role) {
        return createToken(accountId, role, REFRESH_TOKEN_VALID_TIME);
    }

    //토큰 유효성 검사
    public Boolean validateToken(String token) {
        log.info("validateToken method is invoked! token : " + token);
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(baseSecretKey));
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey).build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            throw new InvalidTokenException("SecurityException - Jwt 토큰을 파싱하는 동안 문제가 발생했습니다.");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("MalformedJwtException - Jwt 토큰의 형식이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("ExpiredJwtException - 만료된 Jwt 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("UnsupportedJwtException - 지원하지 않는 Jwt 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("IllegalArgumentException - 잘못된 헤더가 사용되었습니다.");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new InvalidTokenException("SignatureException - Jwt 토큰의 서명이 잘못되었된습니다.");
        }
    }

    public Authentication getAuthentication(String token) { //유효성 검증한 token이 들어옴
        log.info("getAuthentication method is invoked!");
        Claims claims = parseClaims(token);
        try {
            claims.get("accountId");
        } catch (Exception e) {
            try {
                throw new AuthenticationException("Jwt 토큰에 accountId가 존재하지 않습니다.");
            } catch (AuthenticationException ex) {
                ex.printStackTrace();
            }
        }
        Collection<GrantedAuthority> authorities =
                Arrays.stream(claims.get("ROLE_").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        log.info("getAuthentication method - " + "accountId: "+ claims.get("accountId").toString() + ", authorities: " + authorities);
        UserDetails userDetails = new User(claims.get("accountId").toString(), "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // ROLE에 따라 특정 역할과 기본 역할 설정
    private List<GrantedAuthority> extractAuthoritiesFromRole(Role role) {
        List<String> roles = Arrays.asList("ROLE_USER"); // 기본 역할은 USER로 설정

        if (role == Role.SUPERUSER) {
            roles = Arrays.asList("ROLE_SUPERUSER", "ROLE_USER");
        } else if (role == Role.MANAGER) {
            roles = Arrays.asList("ROLE_MANAGER", "ROLE_USER");
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public String resolveToken(String token) {
        if(token.startsWith("Bearer ")) {
            return token.replace("Bearer ", "");
        }
        return null;
    }

    private Claims parseClaims(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(baseSecretKey));
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public String getAccountId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("accountId").toString();
    }

    public String getRole(String token) {
        Claims claims = parseClaims(token);
        return claims.get("role").toString();
    }



    //jwt 토큰 생성
    private TokenInfo createToken(String accountId, Role role, long validTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validTime);
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(baseSecretKey));

        String token = Jwts.builder()  //jwt payload에 저장되는 정보 명시
                .claim("accountId", accountId)
                .claim("ROLE_", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        token = BEARER_TYPE + token;
        return new TokenInfo(token, expiration.getTime()-now.getTime());
    }

    public String getSubject(String token) {
        return Jwts.parser().setSigningKey(baseSecretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
