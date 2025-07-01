package com.rana.weather_by_ip.controller;

import com.rana.weather_by_ip.exceptions.InvalidIpAddressException;
import com.rana.weather_by_ip.exceptions.KeyNotFoundException;
import com.rana.weather_by_ip.exceptions.ServiceUnavailableException;
import com.rana.weather_by_ip.model.response.IpWeatherResponse;
import com.rana.weather_by_ip.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService service;

    @Autowired
    public WeatherController(WeatherService service){
        this.service = service;
    }

    @GetMapping("/weather-by-ip")
    public ResponseEntity<IpWeatherResponse> getWeatherInfo(
            HttpServletRequest request,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false, defaultValue = "C") char tempUnit
    ) throws KeyNotFoundException, InvalidIpAddressException, ServiceUnavailableException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Correlation-ID", MDC.get("X-Correlation-ID"));
        String ipAddress = request.getRemoteAddr();
        logger.info("Started processing request with correlation ID: {}", MDC.get("X-Correlation-ID"));
        if(ip !=null){
              ipAddress = ip;
        }
        logger.info("Received IP address for processing: {}", ipAddress);

        IpWeatherResponse response =  service.generateResponse(ipAddress, tempUnit);
        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }
}
