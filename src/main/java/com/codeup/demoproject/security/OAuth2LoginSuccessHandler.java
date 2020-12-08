package com.codeup.demoproject.security;

import com.codeup.demoproject.models.AuthenticationProvider;
import com.codeup.demoproject.models.CustomOAuth2User;
import com.codeup.demoproject.models.User;
import com.codeup.demoproject.models.UserWithRoles;
import com.codeup.demoproject.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userDao;

    private final PasswordEncoder passwordEncoder;

    public OAuth2LoginSuccessHandler(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        User dbUser = userDao.getUserByEmail(email);
        if(dbUser == null){
            System.out.println("REGISTERING USER");
           //register as new user
            User user = new User(email,email,passwordEncoder.encode("password"));
            user.setAuthProvider(AuthenticationProvider.GOOGLE);
            User regUser = userDao.save(user);
            reAuthenticateUser(dbUser);
            response.sendRedirect("/users/"+regUser.getId()+"edit");
        }else{
            System.out.println("USER EXISTS");
            // update existing customer
            dbUser.setAuthProvider(AuthenticationProvider.GOOGLE);
            userDao.save(dbUser);
            response.sendRedirect("/posts");
        }
//        super.onAuthenticationSuccess(request, response, authentication);
    }

    public void reAuthenticateUser(User regUser){
        UserWithRoles authUser = new UserWithRoles(regUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authUser, authUser.getPassword(), authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}