package com.castanhocorreia.circus.component;

import com.castanhocorreia.circus.service.AuthService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Log4j2
public class TokenFilter extends OncePerRequestFilter {
  @Autowired private TokenProvider tokenProvider;
  @Autowired private AuthService authService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = getTokenHeader(request);
      if (token != null && tokenProvider.validate(token)) {
        String id = tokenProvider.getSubject(token);
        UserDetails userDetails = authService.loadUserById(UUID.fromString(id));
        var authenticationFilter = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        authenticationFilter.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationFilter);
      }
    } catch (Exception exception) {
      logger.error("cannot set user authentication: {}", exception);
    }
    filterChain.doFilter(request, response);
  }

  private String getTokenHeader(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }
}
