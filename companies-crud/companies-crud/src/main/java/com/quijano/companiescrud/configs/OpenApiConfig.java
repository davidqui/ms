package com.quijano.companiescrud.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Company CRUD",
                version = "V1.0.0",
                description = "This is a CRUD for management companies"
                /*contact = @io.swagger.v3.oas.models.info.Contact(
                        name = "Quijano",
                        email = "quijano@example.com",
                        url = "https://github.com/quijano"
                )*/
        )
)
public class OpenApiConfig {
}
