package com.codeup.demoproject.controllers;


import com.codeup.demoproject.models.Ad;
import com.codeup.demoproject.models.Post;
import com.codeup.demoproject.models.User;
import com.codeup.demoproject.repos.PostRepository;
import com.codeup.demoproject.repos.UserRepository;
import com.codeup.demoproject.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    @Autowired
    private PostRepository postDao;
    @Autowired
    private UserRepository userDao;
    @Autowired
    private EmailService emailService;

//    public PostController(PostRepository postDao,UserRepository userDao,EmailService emailService) {
//        this.postDao = postDao;
//        this.userDao = userDao;
//        this.emailService = emailService;
//    }

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
    public String createPost(Model model){
        model.addAttribute("post",new Post());
        return "posts/new";
    }

    @PostMapping("/posts/create")
    public String submitPost(@ModelAttribute Post postToSave){
        User uDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        postToSave.setAuthor(uDb);
        Post pDb = postDao.save(postToSave);
        String subject = "Thank you from Adlister";
        emailService.prepareAndSend(pDb,subject,eBody(pDb));
        return "redirect:/posts/"+pDb.getId();
    }

    @PostMapping("/posts/{id}/delete")
    public String destroyPost(@PathVariable long id){
        Post post = postDao.getOne(id);
        postDao.delete(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String patchPost(@PathVariable(name= "id") long id,Model model){
        model.addAttribute("post",postDao.getOne(id));
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String putPost(@PathVariable(name= "id") long id, @ModelAttribute Post postToPut,Model model){
        Post putPost = postDao.save(postToPut);

        model.addAttribute("post",putPost);
        return "redirect:/posts/"+putPost.getId();
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

    public String eBody(Post post){
       return post.getAuthor().getUsername()+" thank you for creating a post. Your Post can now be seen at http://localhost:8080/posts/"+post.getId();
    }
}
