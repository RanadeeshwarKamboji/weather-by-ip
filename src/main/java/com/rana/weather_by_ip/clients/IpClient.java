package com.rana.weather_by_ip.clients;

import com.rana.weather_by_ip.model.ip_info.IpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ip-service", url = "https://api.ipstack.com")
public interface IpClient {
    @GetMapping("/{ip}")
    IpResponse getIpAddressDetails(
            @PathVariable("ip") String ip,
            @RequestParam String access_key
    );
}
