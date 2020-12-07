package com.codeup.demoproject;

import com.codeup.demoproject.services.UserDetailsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsLoader usersLoader;

    public SecurityConfiguration(UserDetailsLoader usersLoader) {
        this.usersLoader = usersLoader;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usersLoader) // How to find users by their username
                .passwordEncoder(passwordEncoder()) // How to encode and verify passwords
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /* Login configuration */
                .formLogin()
                // Anyone can go to the login page and redirect to user's home page when logged in, it can be any URL
                .loginPage("/login").defaultSuccessUrl("/posts").permitAll()
                /* Logout configuration */
                .and()
                .logout().logoutSuccessUrl("/login?logout") // append a query string value
                .and()
                /* Pages that can be viewed without having to log in */
                // anyone can see the following pages
                .authorizeRequests()
                    .antMatchers("/", "/ads","/posts").permitAll()
                /* Pages that require authentication */
                // only authenticated users can see the following
                    .antMatchers(
                        "/ads/create",
                        "/ads/{id}/*",
                        "/posts/{id}/*",
                        "/ads/delete",
                        "/posts/create",
                        "/posts/delete",
                        "/users/{id}/*"
                    ).authenticated()
                // only users with role can see the following
                    .antMatchers("/admin/*").hasRole("Admin")
        ;
    }
}