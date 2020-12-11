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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        User dbUser = userDao.getUserByEmail(email);
        if(dbUser == null){
            System.out.println("REGISTERING USER");
           //register as new user
            User user = new User(email,email,randomPassword(15));
            user.setAuthProvider(AuthenticationProvider.GOOGLE);
            User regUser = userDao.save(user);
            reAuthenticateUser(regUser);
            System.out.println("USERS ID: "+regUser.getId());
            response.sendRedirect("/users/"+regUser.getId());
        }else{
            System.out.println("GETTING USER");
            // update existing customer
            if(!dbUser.getAuthProvider().toString().equalsIgnoreCase("google")){
                dbUser.setAuthProvider(AuthenticationProvider.GOOGLE);
                dbUser = userDao.save(dbUser);
            }
            reAuthenticateUser(dbUser);
            response.sendRedirect("/posts");
        }
//        super.onAuthenticationSuccess(request, response, authentication);
    }

    public void reAuthenticateUser(User regUser){
        System.out.println("Authenticating Google User from DB");
        UserWithRoles authUser = new UserWithRoles(regUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authUser, authUser.getPassword(), authUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static String randomPassword(int len) {
        System.out.println("IN AUTO HASH");
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
