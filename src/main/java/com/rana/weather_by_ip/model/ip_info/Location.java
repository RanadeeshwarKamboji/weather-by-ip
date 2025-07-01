package com.rana.weather_by_ip.model.ip_info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private int geoname_id;
    private String capital;
    private List<Language> languages;
    private String country_flag;
    private String country_flag_emoji;
    private String country_flag_emoji_unicode;
    private String calling_code;
    private boolean is_eu;
}
