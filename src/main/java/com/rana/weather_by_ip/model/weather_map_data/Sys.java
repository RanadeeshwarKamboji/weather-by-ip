package com.rana.weather_by_ip.model.weather_map_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Sys {
    private String country;
    private long sunrise;
    private long sunset;
}
