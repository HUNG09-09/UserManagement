package project.java.qlsv;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.java.qlsv.service.JwtTokenService;

import java.io.IOException;

// sau khi tao token va tra ve cho nguoi dung, lan sau ng dung se gui token len, su dung filter de loc cac request
@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // JWT thường được gửi dưới dạng Bearer Token, có dạng: Authorization: Bearer <JWT>
        // doc header authorize, posman
        String bearerToken = request.getHeader("Authorization");
        log.info(bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // lay token ra, tru "Bearer ", nen lay tu vi tri so 7, lay phan sau, tren postman
            String username = jwtTokenService.getUsername(token); // neu tra ve khac null, thi cos token, co hieu luc
            if (username != null) {
                /// token valid, create a authentication
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());

                // gia lap security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request,response);
    }




}
