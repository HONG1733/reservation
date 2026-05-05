package com.example.reservation.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {



        String uri = request.getRequestURI();
        String bearer = request.getHeader("Authorization");

        System.out.println("🔥 요청 URI = " + uri);
        System.out.println("🔥 Authorization 헤더 = " + bearer);

        // 1. 인증 제외 경로 (핵심)
        if (uri.startsWith("/api/users/login") ||
            uri.startsWith("/api/users/signup") ||
            uri.startsWith("/api/instructors/login")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 2. 토큰 추출
        String token = resolveToken(request);

        System.out.println("🔥 TOKEN = " + token);

        // 토큰 없으면 그냥 다음 필터로 (Spring security가 처리)
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 토큰 검증
        if (jwtTokenProvider.validateToken(token)) {

            Long userId = Long.parseLong(jwtTokenProvider.getSubject(token));

            String email = jwtTokenProvider.getEmail(token);
            String role = jwtTokenProvider.getRole(token);


            System.out.println("🔥 USER ID = " + userId);
            System.out.println("🔥 EMAIL = " + email);
            System.out.println("🔥 ROLE = " + role);

            //UserDetails 객체 생성
            UserDetails userDetails = User.builder()
                    .username(userId.toString())
                    .password("") // JWT 방식이므로 비밀번호 불필요
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)))
                    .build();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("🔥 AUTH SET 완료");

        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
