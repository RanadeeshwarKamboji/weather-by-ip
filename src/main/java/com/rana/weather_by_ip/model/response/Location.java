package com.rana.weather_by_ip.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String city;
    private String country;

    // Getters and setters
}
