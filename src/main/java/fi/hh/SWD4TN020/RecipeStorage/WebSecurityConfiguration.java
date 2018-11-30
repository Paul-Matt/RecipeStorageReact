package fi.hh.SWD4TN020.RecipeStorage;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import fi.hh.SWD4TN020.RecipeStorage.service.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailServiceImpl userDetailsService; 

	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable().cors().and().authorizeRequests()
		  .antMatchers(HttpMethod.POST, "/login").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        // Filter for the api/login requests
	        .addFilterBefore(new LoginFilter("/login", authenticationManager()),
	                UsernamePasswordAuthenticationFilter.class)
	        // Filter for other requests to check JWT in header
	        .addFilterBefore(new AuthenticationFilter(),
	                UsernamePasswordAuthenticationFilter.class);
	  }
  
	  //  CORS (Cross-Origin Resource Sharing) filter in the security
	  // configuration class. This is needed for the frontend, that is sending requests from
	  // the other origin. 
	  @Bean
	  CorsConfigurationSource corsConfigurationSource() {
	      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	      CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(Arrays.asList("*"));
			config.setAllowedMethods(Arrays.asList("*"));
			config.setAllowedHeaders(Arrays.asList("*"));
			config.setAllowCredentials(true);
	      config.applyPermitDefaultValues();
	      
	      source.registerCorsConfiguration("/**", config);
	      return source;
	}	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

/* @Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //use user entities instead of in-memory users in authentication
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
		 http.cors().and().authorizeRequests().anyRequest().permitAll();   
		 /* temporarily commented out for testing purposes http
	        .authorizeRequests().antMatchers("/css/**", "/", "/recipelist").permitAll() // Enable css when logged out
	        .antMatchers("/delete/{id}").hasRole("ADMIN")
	        .and()
	        .authorizeRequests()
	          .anyRequest().authenticated()
	          .and()
	      .formLogin()
	          .loginPage("/login")
	          .defaultSuccessUrl("/recipelist")
	          .permitAll()
	          .and()
	      .logout()
	          .permitAll(); 
	    }

	    @Autowired
	    //vertailee kryptattuja salasanoja
	    //admin admin käyttis ja salasana
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	    } */

}
