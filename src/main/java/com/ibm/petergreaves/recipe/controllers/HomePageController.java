package com.ibm.petergreaves.recipe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

    @RequestMapping({"/", "/index", "index.html", ""})
    public String doHome(){
        System.out.println("foo1");
        return "index";
    }
}
