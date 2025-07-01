package com.rana.weather_by_ip.model.ip_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Language {
    private String code;
    private String name;
    @JsonProperty("native")
    private String nativeName;
}
