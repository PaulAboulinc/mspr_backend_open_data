package com.pafpsdnc.recipe;

import com.pafpsdnc.recipe.controller.RecipeController;
import com.pafpsdnc.recipe.exception.RecipeNotFound;
import com.pafpsdnc.recipe.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class RecipeApplicationTests {
	@Autowired
	private RecipeController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

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
}