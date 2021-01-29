package com.pafpsdnc.recipe.repository;

import com.pafpsdnc.recipe.model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

}