package com.movies.movierecommendation.config.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.movies.movierecommendation.service.ApiKeyService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String CONTEXT_PATH = "/mra";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final Set<String> WHITELISTED_PATH_PREFIXES = Set.of(
            CONTEXT_PATH + "/swagger-ui",
            CONTEXT_PATH + "/ping",
            CONTEXT_PATH + "/h2-console",
            CONTEXT_PATH + "/v3/api-docs",
            CONTEXT_PATH + "/admin/apikey");

    private static final String API_KEY_HEADER = "X-API-KEY";

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (isWhitelistedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or missing API key\"}");
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken("api_key", "api_key_user",
                        List.of(new SimpleGrantedAuthority("ROLE_API_USER"))));
        filterChain.doFilter(request, response);
    }

    private boolean isWhitelistedPath(String path) {
        return WHITELISTED_PATH_PREFIXES.stream().anyMatch(whitelistedPath -> pathMatcher.match(whitelistedPath + "/**", path));
    }
}
