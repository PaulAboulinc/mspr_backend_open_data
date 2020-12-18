package com.pafpsdnc.recipe.Repository;

import com.pafpsdnc.recipe.Model.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

}