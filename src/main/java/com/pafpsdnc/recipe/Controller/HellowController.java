package com.pafpsdnc.recipe.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HellowController {

    @RequestMapping("/test")
    public String index() {
        return "hello word";
    }

}