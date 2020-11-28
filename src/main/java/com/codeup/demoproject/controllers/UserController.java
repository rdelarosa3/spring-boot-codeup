package com.codeup.demoproject.controllers;
import com.codeup.demoproject.models.Ad;
import com.codeup.demoproject.models.User;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.connection_config.DBConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Random;

@Controller
public class UserController {
//    public static void main(String[] args) {


//        List<User> users = User.findAll();
//        for(User user : users){
//            System.out.println(user.getString("username"));
//        }


//    }
    @GetMapping("/users/index")
    public String index(Model model){
        DBConfiguration.loadConfiguration("/database.properties");
        Base.open();
        User user = User.findById(3);
        user.getId();
        model.addAttribute("user",user);
        Base.close();
        return "/users/index";
    }

//    @GetMapping("/ads")
//    public String ads(Model model){
//        DBConfiguration.loadConfiguration("/database.properties");
//        Base.open();
//        Ad ad = Ad.findFirst("title =?","My Title");
//        model.addAttribute("ad",ad);
//        Base.close();
//        return "/ads";
//    }

//    public static void main(String[] args) {
//        DBConfiguration.loadConfiguration("/database.properties");
//        Ad ad = Ad.findFirst("title =?","My Title");
//
//        Base.close();
//    }

    public static void createUser(){
        DBConfiguration.loadConfiguration("/database.properties");
        Base.open();
        User u = new User();
        u.set("username","oscar");
        u.set("email","email@email.com");
        u.set("password","password");
        u.set("avatar","avatar");
        u.set("role","admin");
        if(u.save()){
            System.out.println("success");
        }else{
            System.out.println(u.errors().get("username"));
        }
    }

    public static void createAd(){
        DBConfiguration.loadConfiguration("/database.properties");
        Base.open();
        Ad a = new Ad();
        a.set("user_id",1);
        a.set("title","This is a Title");
        a.set("description","my description");
        a.set("price",10.00);
        a.set("sold",0);
        a.set("location","san antonio");
        if(a.save()){
            //render success page
        }else{
            System.out.println(a.errors().get("username"));
        }
        User user = User.findFirst("username =?","pedro.juarez");
        Base.close();
    }

//    public static void main(String[] args) {
//        createUser();
//    }
}
