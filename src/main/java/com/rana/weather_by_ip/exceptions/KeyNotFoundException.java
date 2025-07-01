package com.rana.weather_by_ip.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeyNotFoundException extends Exception{
    public String suggestion;
    public  String service;
    public KeyNotFoundException(String errorMessage, String suggestion, String service){
        super(errorMessage);
        this.service = service;
        this.suggestion = suggestion;
    }
}
