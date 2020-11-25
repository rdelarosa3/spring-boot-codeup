package com.codeup.demoproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {
    @GetMapping("/posts") @ResponseBody
    public String index(){
        return "These are the posts";
    }

    @GetMapping("/posts/{id}") @ResponseBody
    public String viewPost(@PathVariable int id){
        return "Here is post "+id;
    }

    @GetMapping("/posts/create") @ResponseBody
    public String createPost(){
        return "Form for new post";
    }

    @PostMapping("/posts/create") @ResponseBody
    public String submitPost(){
        return "Sending your post";
    }
}
