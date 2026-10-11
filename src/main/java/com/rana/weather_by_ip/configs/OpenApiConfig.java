package com.rana.weather_by_ip.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration 
public class OpenApiConfig{

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
            .info(new Info().title("Weather By IP API")
            .description("Current Weather for an IP address via ipStack and WeatherMap.")
            .version("1.0.0"));
    }
}

