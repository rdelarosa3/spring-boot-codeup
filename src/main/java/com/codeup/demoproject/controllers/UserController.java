package com.codeup.demoproject.controllers;

import com.codeup.demoproject.models.User;
import com.codeup.demoproject.repos.PostRepository;
import com.codeup.demoproject.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserRepository userDao;
    @Autowired
    private PostRepository postDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    public UserController(UserRepository userDao, PostRepository postDao,PasswordEncoder passwordEncoder){
//        this.userDao = userDao;
//        this.postDao = postDao;
//        this.passwordEncoder = passwordEncoder;
//    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("users/create")
    public String createProfile(){
        return "users/new";
    }

    @PostMapping("users/create")
    public String submitProfile(@RequestParam Map<String,String> requestParams, Model model){
        User user = new User(requestParams.get("email"),requestParams.get("username"),requestParams.get("password"));
        userDao.save(user);
        return "redirect:users/"+user.getId();
    }

    @GetMapping("users/{id}")
    public String getProfile(@PathVariable long id, Model model){
        User user = userDao.getOne(id);
        model.addAttribute("user",user);
        model.addAttribute("posts", postDao.findAllByAuthor(user));
        return "users/show";
    }

    @GetMapping("users/{id}/edit")
    public String editProfile(@PathVariable long id, Model model){
        model.addAttribute("user",userDao.getOne(id));
        return "users/edit";
    }

    @PostMapping("users/{id}/edit")
    public String patchProfile(@PathVariable long id, Model model){
        model.addAttribute("user",userDao.getOne(id));
        return "redirect:/users/"+id;
    }

    @PostMapping("/users/{id}/delete")
    public String destroyPost(@PathVariable long id){
        userDao.delete(userDao.getOne(id));
        return "redirect:/users";
    }
}
