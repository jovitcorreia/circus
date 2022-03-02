package com.castanhocorreia.circus.component;

import com.castanhocorreia.circus.domain.UserModel;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Log4j2
public class TokenProvider {
  @Value("${auth.token.secret}")
  private String tokenSecret;

  @Value("${auth.token.expiration}")
  private int tokenExpiration;

  public String generate(Authentication authentication) {
    UserModel userPrincipal = (UserModel) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(userPrincipal.getId().toString())
        .setIssuedAt(new Date())
        .setExpiration(new Date(new Date().getTime() + tokenExpiration))
        .signWith(SignatureAlgorithm.HS512, tokenSecret)
        .compact();
  }

  public String getSubject(String token) {
    return Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validate(String token) {
    try {
      Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
      return true;
    } catch (SignatureException exception) {
      log.error("Invalid JWT signature: {}", exception.getMessage());
    } catch (MalformedJwtException exception) {
      log.error("Invalid JWT token: {}", exception.getMessage());
    } catch (ExpiredJwtException exception) {
      log.error("JWT token is expired: {}", exception.getMessage());
    } catch (UnsupportedJwtException exception) {
      log.error("JWT token is unsupported: {}", exception.getMessage());
    } catch (IllegalArgumentException exception) {
      log.error("JWT claims string is empty: {}", exception.getMessage());
    }
    return false;
  }
}
