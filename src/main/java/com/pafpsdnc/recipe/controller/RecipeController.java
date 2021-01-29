package com.pafpsdnc.recipe.controller;

import com.pafpsdnc.recipe.model.Recipe;
import com.pafpsdnc.recipe.repository.RecipeRepository;
import com.pafpsdnc.recipe.exception.RecipeNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recipe findById(@PathVariable Integer id) throws RecipeNotFound {
        return recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Recipe> findRecipes() {
        return recipeRepository.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe createRecipe() {
        Recipe recipe = new Recipe();
        recipe.setPortions(1000);
        recipe.setName("name");
        recipe.setDescription("description");

        recipeRepository.save(recipe);

        return recipe;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Recipe updateRecipe(@PathVariable("id") Integer id) throws RecipeNotFound {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);

        recipe.setName("name updated !!!");
        recipe.setDescription("description updated !!!");
        recipeRepository.save(recipe);

        return recipe;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRecipe(@PathVariable("id") Integer id) throws RecipeNotFound {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFound::new);
        recipeRepository.delete(recipe);

        return "La recette " + recipe.getId() + " a été supprimée";
    }
}