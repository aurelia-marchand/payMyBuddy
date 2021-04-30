package com.payMyBuddy.buddy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.payMyBuddy.buddy.service.UserBuddyServiceI;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

  
  @Autowired
  private UserBuddyServiceI userService;
  
 
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
}
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
    auth.setUserDetailsService(userService);
    auth.setPasswordEncoder(passwordEncoder());
    return auth;
}
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .httpBasic().and()
      .authorizeRequests()
      .antMatchers("/admin").hasRole("ADMIN")
      .antMatchers("/admin").hasRole("ADMIN")
      .antMatchers("/home", "/transfer").hasAnyRole("ADMIN","USER")
      .antMatchers("/login", "/suscribe", "/css/**").permitAll()
      .and()
      .formLogin().loginPage("/login")
      .defaultSuccessUrl("/home", true)
      .failureUrl("/login?error=true")
      .and()
      .logout().deleteCookies("JSESSIONID")
      .invalidateHttpSession(true)
      .clearAuthentication(true)
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .logoutSuccessUrl("/login?logout");
      
  }
  
 
}
