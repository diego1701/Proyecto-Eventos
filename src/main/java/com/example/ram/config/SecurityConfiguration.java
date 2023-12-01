package com.example.ram.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.ram.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserService userService;

    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider());

    }
    
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(
                "/registration**",          
                "/css/**",
                "/img/**",
                "/Javas/**",
                "/", 
                "/indexx",
                "/acercade",
                "/contactanos",
		"/blogs/userblogs",
		"/blogs/verImagen/{id}",
                "/salones/userSalones",
                "/salones/VerImagen/{id}").permitAll()
            .antMatchers("/login/google", "/oauth2/authorization/google").permitAll()
            .antMatchers("/index").authenticated()
            .antMatchers("/gestionAdmin").authenticated()
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    for (GrantedAuthority auth : authentication.getAuthorities()) {
                        if (auth.getAuthority().equals("USER")) {
                            response.sendRedirect("/index");
                            return;
                        } else if (auth.getAuthority().equals("ADMIN")) {
                            response.sendRedirect("/gestionAdmin");
                            return;
                        }
                    }
                })
                .permitAll()

            .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/index", true)
                .userInfoEndpoint()
                .oidcUserService(oidcUserService());
    }
    
    
    
    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }




}
