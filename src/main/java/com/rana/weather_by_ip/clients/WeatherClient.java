package com.rana.weather_by_ip.clients;

import com.rana.weather_by_ip.model.weather_map_data.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-service", url = "https://api.openweathermap.org")
public interface WeatherClient {
    @GetMapping("/data/2.5/weather")
    WeatherResponse getWeatherData(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String appid,
            @RequestParam String units
    );
}
