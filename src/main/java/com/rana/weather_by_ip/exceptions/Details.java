package com.rana.weather_by_ip.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    private String service;
    private String issue;
    private String suggestion;
}
