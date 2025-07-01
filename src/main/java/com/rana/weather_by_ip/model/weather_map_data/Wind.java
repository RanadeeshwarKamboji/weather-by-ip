package com.rana.weather_by_ip.model.weather_map_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Wind {
    private double speed;
    private int deg;
    private double gust;
}
