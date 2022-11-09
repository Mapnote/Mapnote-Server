package com.mapnote.mapnoteserver.security.config;

import com.mapnote.mapnoteserver.security.CustomUserDetailService;
import com.mapnote.mapnoteserver.security.jwt.JwtAuthenticationFilter;
import com.mapnote.mapnoteserver.security.jwt.JwtEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtEntryPoint jwtEntryPoint;
  private final CustomUserDetailService customUserDetailService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable()
        .csrf().disable()
        .logout().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**").permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/users", "/api/v1/users/login", "/api/v1/users/reissue", "/api/v1/users/logout").permitAll()
        .antMatchers(HttpMethod.GET, "/api/v1/users", "/api/v1/map/**", "/api/v1/schedule/**").hasAnyRole("USER")
        .antMatchers(HttpMethod.PUT, "/api/v1/users/newPassword", "/api/v1/users", "/api/v1/schedule/**").hasAnyRole("USER")
        .antMatchers(HttpMethod.DELETE, "/api/v1/users", "/api/v1/schedule").hasAnyRole("USER")
        .antMatchers(HttpMethod.POST, "api/v1/schedule").hasAnyRole("USER")
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtEntryPoint)
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
  }
}
