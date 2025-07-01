package com.rana.weather_by_ip.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IpWeatherResponse {
    private String ip;
    private Location location;
    private Weather weather;

    // Getters and setters
}
