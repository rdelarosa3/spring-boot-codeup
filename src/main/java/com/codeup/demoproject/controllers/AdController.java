package com.codeup.demoproject.controllers;

import com.codeup.demoproject.models.Ad;
import com.codeup.demoproject.models.User;
import com.codeup.demoproject.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.codeup.demoproject.repos.AdRepository;

import java.util.List;
import java.util.Map;

@Controller
public class AdController {
    @Autowired
    private AdRepository adDao;
    @Autowired
    private UserRepository userDao;

//    public AdController(AdRepository adDao,UserRepository userDao){
//        this.adDao = adDao;
//        this.userDao = userDao;
//    }

    @GetMapping("/ads")
    public String index(Model model){
        model.addAttribute("ads",adDao.findAll());
        return "ads/index";
    }

    @GetMapping("/ads/search")
    public String search(@RequestParam(name = "term") String term, Model viewModel){
        term = "%"+term+"%";
        List<Ad> dbAds = adDao.findAllByTitleIsLike(term);
        viewModel.addAttribute("ads", dbAds);
        return "ads/index";
    }

    @GetMapping("/ads/{id}")
    public String show(@PathVariable long id, Model model){
        model.addAttribute("ad",adDao.getOne(id));
    return "ads/show";
    }

    @GetMapping("/ads/create")
    public String showCreateForm(Model model){
        model.addAttribute("ad",new Ad());
        return "ads/new";
    }

    @PostMapping("/ads/create")
    public String createAd(@ModelAttribute Ad adToSave){
        User uDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        adToSave.setOwner(uDb);
        Ad dbAd = adDao.save(adToSave);
        return "redirect:/ads/"+dbAd.getId();
    }
}


