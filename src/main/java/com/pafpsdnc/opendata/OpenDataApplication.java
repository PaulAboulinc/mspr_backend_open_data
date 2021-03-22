package com.pafpsdnc.opendata;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpenDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenDataApplication.class, args);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("/"));
    }
}
