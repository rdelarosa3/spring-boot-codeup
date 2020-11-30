package com.codeup.demoproject.controllers;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    @GetMapping("/posts")
    public String index(Model model){
        Faker faker = new Faker();
        List<Post> posts = new ArrayList<>();
        for(int i = 1; i <= 20; i++){
            posts.add(new Post(
                    (long) i,
                    faker.book().title(),
                    faker.shakespeare().asYouLikeItQuote()
            ));
        }
        model.addAttribute("posts",posts);
        return "/posts/index";
    }

    @GetMapping("/posts/{id}/{title}/{description}")
    public String viewPost(@PathVariable long id, @PathVariable String title, @PathVariable String description, Model model ){
        model.addAttribute("id",id);
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        return "/posts/show";
    }

    @GetMapping("/posts/create")
    public String createPost(){
        return "/posts/new";
    }

    @PostMapping("/posts/create")
    public String submitPost(@RequestParam(name ="title") String title, @RequestParam(name ="description") String description, Model model){
        model.addAttribute("title",title);
        model.addAttribute("description");
        return "/posts/show";
    }
}
