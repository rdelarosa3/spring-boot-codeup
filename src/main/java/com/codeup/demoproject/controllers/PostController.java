package com.codeup.demoproject.controllers;


import com.codeup.demoproject.models.Post;
import com.codeup.demoproject.repos.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
public class PostController {
    private final PostRepository postDao;

    public PostController(PostRepository postDao) {
        this.postDao = postDao;
    }

    @GetMapping("/posts")
    public String index(Model model){
        model.addAttribute("posts",postDao.findAll());
        return "posts/index";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable(name= "id") long id,  Model model ){
        model.addAttribute("post",postDao.getOne(id));
        return "posts/show";
    }

    @GetMapping("/posts/create")
    public String createPost(){
        return "posts/new";
    }

    @PostMapping("/posts/create")
    public String submitPost(@RequestParam Map<String, String> requestParams){
        Post p = new Post(requestParams.get("title"),requestParams.get("description"));
        postDao.save(p);
        return "redirect:/posts/"+p.getId();
    }

    @PostMapping("/posts/{id}/delete")
    public String destroyPost(@PathVariable long id){
        Post post = postDao.getOne(id);
        postDao.delete(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String putPost(@PathVariable(name= "id") long id,Model model){
        model.addAttribute("post",postDao.getOne(id));
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String patchPost(@PathVariable(name= "id") long id, @RequestParam Map<String, String> requestParams,Model model){
        Post post = postDao.getOne(id);
        post.setTitle(requestParams.get("title"));
        post.setDescription(requestParams.get("description"));
        postDao.save(post);
        model.addAttribute("post",post);
        return "redirect:/posts/"+post.getId();
    }

}
