package com.pafpsdnc.recipe;

import com.pafpsdnc.recipe.Controller.HellowController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class RecipeApplicationTests {
	@Autowired
	private HellowController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
}