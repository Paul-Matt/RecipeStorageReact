package fi.hh.SWD4TN020.RecipeStorage;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.hh.SWD4TN020.RecipeStorage.domain.AccountCredentials;
import fi.hh.SWD4TN020.RecipeStorage.service.AuthenticationService;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

  public LoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  // Authentication is performed by
  // the attemptAuthentication method. If the authentication is successful, the
  // succesfulAuthentication method is executed. 
  @Override
  public Authentication attemptAuthentication(
	HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
	AccountCredentials creds = new ObjectMapper()
        .readValue(req.getInputStream(), AccountCredentials.class);
	return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(
            creds.getUsername(),
            creds.getPassword(),
            Collections.emptyList()
        )
    );
  }

  // succesfulAuthentication method will call
  // the addToken method in the service class and the token will be added to
  // the Authorization header
  @Override
  protected void successfulAuthentication(
      HttpServletRequest req,
      HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {
	  AuthenticationService.addToken(res, auth.getName());
  }
}
