package com.example.SmartCart.common.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI smartCartOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartCart API")
                        .description("REST API Documentation for SmartCart")
                        .version("1.0"));
    }
}
