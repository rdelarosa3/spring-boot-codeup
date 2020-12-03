package com.codeup.demoproject.controllers;


import com.codeup.demoproject.models.Ad;
import com.codeup.demoproject.models.Post;
import com.codeup.demoproject.models.User;
import com.codeup.demoproject.repos.PostRepository;
import com.codeup.demoproject.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    private final PostRepository postDao;
    private final UserRepository userDao;

    public PostController(PostRepository postDao,UserRepository userDao) {
        this.postDao = postDao;
        this.userDao = userDao;
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
        User u = userDao.getOne(1L);
        Post p = new Post(requestParams.get("title"),requestParams.get("description"),u);
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

    @GetMapping("/posts/search")
    public String search(){
        return "posts/search";
    }
    @PostMapping("/posts/search")
    public String submitSearch(@RequestParam(name = "term") String term, Model viewModel){
        term = "%"+term+"%";
        List<Post> dbPosts = postDao.findAllByTitleLikeOrDescriptionLike(term,term);
        viewModel.addAttribute("posts", dbPosts);
        return "posts/index";
    }

}
