package com.pafpsdnc.recipe.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HellowController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}