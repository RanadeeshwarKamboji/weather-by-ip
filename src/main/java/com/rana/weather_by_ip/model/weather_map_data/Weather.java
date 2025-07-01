package com.rana.weather_by_ip.model.weather_map_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}
