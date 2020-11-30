package com.codeup.demoproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index page";
    }

    @GetMapping("/home")
    public String welcome(){
        return "home";
    }
    @GetMapping("/home/{name}")
    public String person(@PathVariable String name, Model model){
        model.addAttribute("name",name);
        return "home";
    }

    @GetMapping("/join")
    public String join(Model model){
        List<String> cohortNames = new ArrayList<>();
        cohortNames.add("COBOL");
        cohortNames.add("Draco");
        model.addAttribute("cohortNames",cohortNames);
        return "join";
    }

    // get params from url
    @PostMapping("/join")
    public String postJoin(@RequestParam(name ="cohort") String cohort, Model model){

        model.addAttribute("cohort",cohort);
        return  "join" ;
    }
}
