package com.smlikelion.webfounder.security;

import com.smlikelion.webfounder.security.exception.EmptyTokenException;
import com.smlikelion.webfounder.security.exception.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("JwtAuthenticationFilter - doFilterInternal is invoked!");

        try {
            // ✅ OPTIONS 요청은 필터를 건너뛰고 다음 필터로 진행
            if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
                log.info("OPTIONS 요청이므로 필터를 건너뜁니다.");
                filterChain.doFilter(request, response);
                return;
            }

            // ✅ 특정 경로 필터링 (JWT 인증 없이 접근 가능)
            String path = request.getRequestURI();
            if (path.startsWith("/health-check") || path.startsWith("/security-check") || path.startsWith("/reissue") || path.startsWith("/docs/quest")) {
                log.info("JWT 인증을 건너뛰는 경로입니다: " + path);
                filterChain.doFilter(request, response);
                return;
            }

            String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
            if (bearerToken == null) {
                throw new EmptyTokenException("Authorization 헤더에 토큰이 없습니다.");
            }
            if (!bearerToken.startsWith("Bearer ")) {
                throw new InvalidTokenException("토큰 형태가 올바르지 않습니다.");
            }

            String jwtToken = jwtTokenProvider.resolveToken(bearerToken); // Bearer 제거
            if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {
                // 토큰이 유효한 경우에만 Authentication 객체를 가져옴
                Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
                if (authentication != null) {
                    log.info("doFilterInternal 메서드에서 Authentication 객체를 가져왔습니다.");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new InvalidTokenException("토큰 유효성을 검증한 후 Authentication 객체를 가져오지 못했습니다.");
                }
            } else {
                throw new InvalidTokenException("토큰 유효성을 검증하지 못했습니다.");
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.");
            throw new InvalidTokenException("토큰이 만료되었습니다.");
        } catch (NullPointerException e) {
            log.error("Authorization 헤더에 토큰이 없습니다.");
            throw new EmptyTokenException("Authorization 헤더에 토큰이 없습니다.");
        }
    }
}
