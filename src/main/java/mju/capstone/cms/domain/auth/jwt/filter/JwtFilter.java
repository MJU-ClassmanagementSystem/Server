package mju.capstone.cms.domain.auth.jwt.filter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.capstone.cms.domain.auth.jwt.provider.JwtProvider;
import mju.capstone.cms.domain.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtProvider jwtProvider;
    /**
     * 요청 시 마다 토큰 확인해서 request header 에 id/type 설정
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        //토큰 오류시 실패.
        if (bearerToken == null || !bearerToken.startsWith("Bearer ") || !jwtProvider.validateToken(bearerToken)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized");
            return;
        }

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return;
        }
        if (CorsUtils.isPreFlightRequest(request)) {
            return;
        }

        if (request.getMethod().equals("OPTIONS")) {
            return;
        }
        String jwt = request.getHeader("Authorization");

        //토큰에서 id 추출
        String id = jwtProvider.extractId(bearerToken);
        //토큰에서 type 추출
        String type = jwtProvider.extractType(bearerToken);

        //request attribute 설정.
        request.setAttribute("id", id);
        request.setAttribute("type", type);
        filterChain.doFilter(request, response);
    }
}
