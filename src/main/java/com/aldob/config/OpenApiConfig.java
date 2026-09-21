package com.aldob.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Aldo Becerra",
            email = "aldobecerra1609@gmail.com"
        ),
        description = "Ticket System API",
        title = "Ticket System",
        version = "1.0.0"
    )
)
public class OpenApiConfig {


}
