package com.rana.weather_by_ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class WeatherByIpApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherByIpApplication.class, args);
	}

}
