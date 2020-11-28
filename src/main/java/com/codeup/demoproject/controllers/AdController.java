package com.codeup.demoproject.controllers;

import com.codeup.demoproject.models.Ad;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.connection_config.DBConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AdController {
    @GetMapping("/ads") @ResponseBody
    public String index(Model model){
        List<Ad> ads = Ad.findAll();
        model.addAttribute("ads",ads);
        return "ads/index";
    }

    @GetMapping("/ads/{id}")
    public String showAd(@PathVariable long id,Model model){
        DBConfiguration.loadConfiguration("/database.properties");
        Base.open();
        model.addAttribute("ad", Ad.findFirst("id =?",id));
        Base.close();
        return "ads/show";
    }

    @GetMapping("/ads/new")
    public String createAd(){
        return "ads/new";
    }

    @PostMapping("/ads/new")
    public String submitAd(@RequestParam Map<String, String> requestParams,Model model){

        Ad ad = new Ad();
        ad.fromMap(requestParams);
        System.out.println(ad.getString("title"));
        System.out.println(ad.getString("description"));
        System.out.println(ad.getString("price"));
        System.out.println(ad.getString("location"));
        DBConfiguration.loadConfiguration("/database.properties");
        Base.open();
        if(ad.save()){
            model.addAttribute("ad",ad);
            System.out.println(ad.getId());
            Base.close();
            return "redirect:/ads/"+ad.getId();
        }else{
            model.addAttribute("errors", ad.errors());
            Base.close();
            return "ads/new";
        }

    }

    @GetMapping("/ads/{id}/edit")
    public String createAd(@PathVariable(name ="id") long id, Model model){
        model.addAttribute("ad", Ad.findFirst("id =?",id));
        return "ads/edit";
    }

    @PostMapping("/ads/{id}/edit")
    public String submitAd(@PathVariable(name ="id") long id, Model model){
        model.addAttribute("ad", Ad.findFirst("id =?",id));
        return "ads/edit";
    }


}
