package com.pafpsdnc.recipe;

import com.pafpsdnc.recipe.controller.RecipeController;
import com.pafpsdnc.recipe.exception.RecipeNotFound;
import com.pafpsdnc.recipe.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RecipeApplicationTests {
	@Autowired
	private RecipeController controller;

	@Test
	void testModelRecipe() {
		Recipe recipe = new Recipe();
		recipe.setName("test");
		recipe.setDescription("test");
		recipe.setPortions(1);
		assertThat(recipe.getName()).isEqualTo("test");
		assertThat(recipe.getDescription()).isEqualTo("test");
		assertThat(recipe.getPortions()).isEqualTo(1);
	}

	@Test
	void testModelRecipeController() throws RecipeNotFound {
		assertThat(controller).isNotNull();

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("portions", 1000);
		requestBody.put("name", "name");
		requestBody.put("description", "description");

		Recipe recipe = controller.createRecipe(requestBody);
		assertThat(recipe).isNotNull();

		requestBody.put("name", "name updated");
		recipe = controller.updateRecipe(recipe.getId(), requestBody);
		assertThat(recipe.getName()).isEqualTo("name updated");

		String message = "La recette " + recipe.getId() + " a bien été supprimée";
		assertThat(controller.deleteRecipe(recipe.getId())).isEqualTo(message);
	}

	@Test
	void testException() {
		RecipeNotFound recipeNotFound = new RecipeNotFound();
		assertThat(recipeNotFound.getMessage()).isEqualTo("La recette n'a pas été trouvée");

		RecipeNotFound recipeNotFound2 = new RecipeNotFound("test");
		assertThat(recipeNotFound2.getMessage()).isEqualTo("test");
	}
}