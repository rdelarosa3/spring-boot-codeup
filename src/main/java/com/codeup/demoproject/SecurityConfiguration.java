package com.codeup.demoproject;

import com.codeup.demoproject.services.UserDetailsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsLoader usersLoader;

//    public SecurityConfiguration(UserDetailsLoader usersLoader) {
//        this.usersLoader = usersLoader;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(usersLoader);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider())
//                codeup method to authenticate
                // How to find users by their username
//                .userDetailsService(usersLoader)
                // How to encode and verify passwords
//                .passwordEncoder(passwordEncoder())
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                /* Pages that can be viewed without having to log in */
                    .antMatchers("/", "/ads","/posts").permitAll()
                /* Pages that require authentication */
                    .antMatchers(
                        "/ads/create",
                        "/ads/{id}/*",
                        "/posts/{id}/*",
                        "/ads/delete",
                        "/posts/create",
                        "/posts/delete",
                        "/users/{id}/*"
                    ).authenticated()
                /* Pages that require a role */
                    .antMatchers("/admin/*").hasRole("Admin")
                .and()
                /* Login configuration */
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/posts").permitAll()  //  all can access login page & on success redirect, it can be any URL
                /* Logout configuration */
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout") // append a query string value
                .and()
                /* remember me feature */
                .rememberMe()
                .tokenValiditySeconds(2592000)// sets expiration of token for remember me

                /* customizing login form urls or params */
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/sigin")
//                .loginPage("/login").permitAll()
//                .usernameParameter("custom-username-param")
//                .passwordParameter("custom-password-param")
//                .and()
//                .rememberMe().rememberMeParameter("custom-remember-me-param")
                
        ;
    }
}