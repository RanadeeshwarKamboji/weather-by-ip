package com.rana.weather_by_ip.model.ip_info;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IpStackError {
    private int code;
    private String type;
    private String info;
}

