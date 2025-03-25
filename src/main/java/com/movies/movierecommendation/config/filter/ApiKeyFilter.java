package com.movies.movierecommendation.config.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.movies.movierecommendation.service.ApiKeyService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String CONTEXT_PATH = "/mra";

    private static final String SWAGGER_UI_PATH = CONTEXT_PATH + "/swagger-ui";

    private static final String PING_PATH = CONTEXT_PATH + "/ping";

    private static final String API_DOCS_PATH = CONTEXT_PATH + "/v3/api-docs";

    private static final String DB_CONSOLE_PATH = CONTEXT_PATH + "/h2-console";

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith(SWAGGER_UI_PATH)
                || path.startsWith(PING_PATH)
                || path.startsWith(DB_CONSOLE_PATH)
                || path.startsWith(API_DOCS_PATH)) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authenticate = SecurityContextHolder.getContext().getAuthentication();
        if (authenticate != null && authenticate.isAuthenticated()) {
            if (authenticate.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        String apiKey = request.getHeader("Authorization");
        if (apiKey != null && apiKey.startsWith("Bearer-")) {
            apiKey = apiKey.substring(7);
            if (!apiKeyService.isValidApiKey(apiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid or expired API key\"}");
                return;
            }
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("admin", null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"API key required\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
