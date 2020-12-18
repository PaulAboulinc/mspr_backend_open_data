package com.pafpsdnc.recipe.Controller;

import com.pafpsdnc.recipe.Model.Recipe;
import com.pafpsdnc.recipe.Repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HellowController {
    @Autowired
    private RecipeRepository recipeRepository;

    @RequestMapping("/test")
    public String index() {
        return "hello word !!! test test";
    }

    @RequestMapping("/add")
    public String add() {
        Recipe recipe = new Recipe();
        recipe.setName("Cookies");
        recipe.setDescription("Délicieux cookies aux pépites de chocolats !");

        recipeRepository.save(recipe);

        return "Ajout effectué !!";
    }

}