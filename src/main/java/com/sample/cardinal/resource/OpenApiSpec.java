package com.sample.cardinal.resource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(info = @Info(
        title = "Cardinal Service API",
        description = "A template API ready to be extended!",
        version = "1"
))

/*
Remove if your application does not use auth.  By default the Rabbit agent is enabled and thus
basicAuth is enabled.
 */
@SecuritySchemes({
        @SecurityScheme(
                name = "basicAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "basic"
        )
})
public class OpenApiSpec  {

}
