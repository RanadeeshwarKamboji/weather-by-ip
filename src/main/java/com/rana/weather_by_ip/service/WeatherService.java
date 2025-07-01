package com.rana.weather_by_ip.service;

import com.rana.weather_by_ip.clients.IpClient;
import com.rana.weather_by_ip.clients.WeatherClient;
import com.rana.weather_by_ip.exceptions.InvalidIpAddressException;
import com.rana.weather_by_ip.exceptions.KeyNotFoundException;
import com.rana.weather_by_ip.exceptions.ServiceUnavailableException;
import com.rana.weather_by_ip.model.ip_info.IpResponse;
import com.rana.weather_by_ip.model.response.IpWeatherResponse;
import com.rana.weather_by_ip.model.response.Location;
import com.rana.weather_by_ip.model.response.Weather;
import com.rana.weather_by_ip.model.weather_map_data.WeatherResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.rana.weather_by_ip.utilities.Utilities;

@Service
public class WeatherService {

    private final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final Environment env;

    private final IpClient ipClient;
    private final WeatherClient weatherClient;

    @Autowired
    public WeatherService(IpClient ipClient, WeatherClient weatherClient, Environment env) {
        this.ipClient = ipClient;
        this.weatherClient = weatherClient;
        this.env = env;
    }

    private IpResponse getIpAddressInfo(String ip) throws KeyNotFoundException, ServiceUnavailableException {
        String accessKey = env.getProperty("IP_STACK_API_KEY");
        if(accessKey == null){
            logger.info("Configuration error: IpStack API key ('access_key') not found in environment variables.");
            throw new KeyNotFoundException("Missing IpStack API key.",
                    "Please provide a valid 'access_key' to authenticate your request.",
                    "Internal Application System.");
        }
        try {
            IpResponse ipResponse = ipClient.getIpAddressDetails(ip, accessKey);
            if (!ipResponse.isSuccess() && ipResponse.getError() !=null && ("invalid_access_key".equals(ipResponse.getError().getType()))) {
                logger.warn("IP lookup failed: Unable to retrieve IP address details from IpStack. Possible cause — missing or invalid 'access_key'.");
                throw new ServiceUnavailableException("The IpStack service is currently unreachable or returning errors. Please try again later",
                        "Please ensure your application's API key (access_key) for IpStack is correct.",
                        "IpStack");
                }

            logger.info("IP lookup completed: Successfully retrieved IP address details from IpStack for '{}'.", ip);
            return ipResponse;

        }
        catch(FeignException e){
            logger.warn("IP lookup failed: Unable to retrieve IP address details from IpStack.");
            throw new ServiceUnavailableException("The IpStack service is currently unreachable or returning errors. Please try again later",
                    "Please ensure your application's API key (access_key) for IpStack is correct.",
                    "IpStack");
        }
    }

    private WeatherResponse getWeatherInfo(double lat, double lon, String units) throws KeyNotFoundException, ServiceUnavailableException {
        String appId = env.getProperty("WEATHER_MAP_API_KEY");
        if(appId == null){
            logger.warn("Configuration error: OpenWeatherMap API key ('appid') not found in environment variables. Please verify setup.");
            throw new KeyNotFoundException("Missing OpenWeatherMap API key.",
                    "Please provide a valid 'appid' to authenticate your request.",
                    "Internal Application System");
        }
        try {
            WeatherResponse weatherResponse = weatherClient.getWeatherData(lat, lon, appId, units);
            logger.info("Weather lookup completed: Successfully retrieved weather data from OpenWeatherMap for the provided IP address.");
            return weatherResponse;
        }
        catch (FeignException e){
            logger.error("Weather lookup failed: Unable to retrieve weather data from OpenWeatherMap for the provided IP address.");
            throw new ServiceUnavailableException("The OpenWeatherMap service is currently unreachable or returning errors. Please try again later",
                    "Please ensure your application's API key (App ID) for OpenWeatherMap is correct.",
                    "OpenWeatherMap");
        }
    }

    @Cacheable(value = "ipCache", key = "#ip")
    public IpWeatherResponse generateResponse(String ip, char unit) throws KeyNotFoundException, InvalidIpAddressException, ServiceUnavailableException {
        boolean isIpValid = Utilities.isValidIp(ip);
        if(!isIpValid){
            logger.error("Validation failed: The provided IP address '{}' is either invalid or missing.", ip);
            throw new InvalidIpAddressException("The IP address provided is either empty or invalid",
                    "Please provide valid ip address",
                    "Client Request Data");
        }
        logger.info("Validation Successful: The provided ip address {} is valid", ip);
        String units = Utilities.getUnits(unit);

        if("metric".equals(units)){
            unit = 'C';
        }


        IpResponse ipResponse = getIpAddressInfo(ip);

        double lat = ipResponse.getLatitude();
        double lon = ipResponse.getLongitude();
        if(lat == 0.0 || lon == 0.0){
            logger.warn("Geolocation error: Provided IP address lacks valid latitude and longitude coordinates.");
            throw new InvalidIpAddressException("Latitude and/or Longitude value is missing",
                    "Please provide valid ip address",
                    "Client Request Data");
        }

        WeatherResponse weatherResponse = getWeatherInfo(lat, lon, units);

        Location location = new Location();
        Weather weather = new Weather();

        String city = ipResponse.getCity();
        String country = ipResponse.getCountry_name();
        location.setCity(city);
        location.setCountry(country);

        double temp = weatherResponse.getMain().getTemp();
        int humidity = weatherResponse.getMain().getHumidity();
        String description = weatherResponse.getWeather().get(0).getDescription();
        String tempUnit = unit=='K'?"K":"°"+unit;
        weather.setTemperature(temp + tempUnit);
        weather.setHumidity(humidity);
        weather.setDescription(description);
        logger.info("Response generation succeeded: Desired output created successfully.");

        return new IpWeatherResponse(ip, location, weather);

    }
}
