package com.movies.movierecommendation.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(name = "X-API-KEY", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "X-API-KEY")
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@Configuration
public class OpenApiConfig {}
