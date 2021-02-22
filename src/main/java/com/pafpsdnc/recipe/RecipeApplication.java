package com.pafpsdnc.recipe;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeApplication.class, args);
	}

	@Bean
	public OpenAPI springShopOpenAPI(
			@Value("${info.app.version}") String appVersion,
			@Value("${server.servlet.context-path}") String contextPath
	) {
		System.out.println(appVersion);
		System.out.println(contextPath);
		return new OpenAPI()
				.addServersItem(new Server().url(contextPath))
				.info(new Info()
						.title("Lala")
						.description("Lala")
						.version(appVersion))
				.components(new Components()
						.addSecuritySchemes("basicScheme",
								new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));
	}
}
