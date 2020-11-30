package com.codeup.demoproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Random;

@Controller
public class DiceController {
    @GetMapping("/roll-dice")
    public String diceRoll(){
          return "dice/roll-dice";
    }

    @GetMapping("/roll-dice/{side}")
    public String getSide(@PathVariable(name ="side") int side, Model model){
        model.addAttribute("side",side);
        Random rand = new Random();
        if(side == rand.nextInt(6)+1){
            model.addAttribute("isCorrect","yes");
        }
        return "dice/show-side";
    }
}