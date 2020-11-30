package com.codeup.demoproject.controllers;

import com.codeup.demoproject.models.Post;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    public Faker faker = new Faker();
    public List<Post> posts = new ArrayList<>();

    public void seedPosts(){
        for(int i = 1; i <= 20; i++){
            posts.add(new Post(
                    i,
                    faker.book().title(),
                    faker.shakespeare().asYouLikeItQuote()
            ));
        }
    }

    @GetMapping("/posts")
    public String index(Model model){
        if(posts.size()<1) seedPosts();
        model.addAttribute("posts",posts);
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable int id,  Model model ){
        Post post = posts.get(id-1);
        model.addAttribute("post",post);
        return "posts/show";
    }

    @GetMapping("/posts/create")
    public String createPost(){
        return "posts/new";
    }

    @PostMapping("/posts/create")
    public String submitPost(@RequestParam Map<String, String> requestParams){
        Post p = new Post(posts.size()+1,requestParams.get("title"),requestParams.get("description"));
        posts.add(p);
        if (posts.get(posts.size()-1).getId() == p.getId()){
            System.out.println("this is my post id :"+p.getId());
            return "redirect:/posts/"+p.getId();
        }
        return "posts/new";
    }
}
