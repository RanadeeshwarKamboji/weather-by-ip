package com.rana.weather_by_ip.model.ip_info;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IpResponse {
    private String ip;
    private String type;
    private String continent_code;
    private String continent_name;
    private String country_code;
    private String country_name;
    private String region_code;
    private String region_name;
    private String city;
    private String zip;
    private double latitude;
    private double longitude;
    private String msa;
    private String dma;
    private String radius;
    private String ip_routing_type;
    private String connection_type;
    private Location location;
    private boolean success;
    private IpStackError error;


}


