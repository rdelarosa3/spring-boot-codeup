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
//        DBConfiguration.loadConfiguration("/database.properties");
//        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/active_jdbc_db", "root", "root");
//        Base.open();
//        User u = new User();
//        u.set("username","juan");
//        u.set("email","email@email.com");
//        u.set("password","password");
//        u.set("avatar","avatar");
//        u.set("role","admin");
//        u.insert();

//        List<User> users = User.findAll();
//        for(User user : users){
//            System.out.println(user.getString("username"));
//        }

//        Ad a = new Ad();
//        a.set("user_id",1);
//        a.set("title","This is a Title");
//        a.set("description","my description");
//        a.set("price",10.00);
//        a.set("sold",0);
//        a.set("location","san antonio");
//        a.insert();
//        User user = User.findFirst("username =?","pedro.juarez");
//        Base.close();
//    }
    @GetMapping("/users/index")
    public String index(Model model){
        DBConfiguration.loadConfiguration("/database.properties");
        Base.open();
        User user = User.findById(2);
        model.addAttribute("user",user);
        Base.close();
        return "/users/index";
    }
}
