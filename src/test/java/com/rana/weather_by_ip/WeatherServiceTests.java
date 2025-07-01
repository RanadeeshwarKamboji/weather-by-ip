package com.rana.weather_by_ip;

import com.rana.weather_by_ip.clients.IpClient;
import com.rana.weather_by_ip.clients.WeatherClient;
import com.rana.weather_by_ip.exceptions.InvalidIpAddressException;
import com.rana.weather_by_ip.exceptions.KeyNotFoundException;
import com.rana.weather_by_ip.exceptions.ServiceUnavailableException;
import com.rana.weather_by_ip.model.ip_info.IpResponse;
import com.rana.weather_by_ip.model.response.IpWeatherResponse;
import com.rana.weather_by_ip.model.weather_map_data.Main;
import com.rana.weather_by_ip.model.weather_map_data.Weather;
import com.rana.weather_by_ip.model.weather_map_data.WeatherResponse;
import com.rana.weather_by_ip.service.WeatherService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTests {

    @Mock
    private IpClient ipClient; // Mock the external IP service client

    @Mock
    private WeatherClient weatherClient; // Mock the external Weather service client

    @Mock
    private Environment env; // Mock the Spring Environment to control properties

    @InjectMocks
    private WeatherService weatherService; // Inject mocks into WeatherService

    // Common data for successful tests
    private static final String TEST_IP = "8.8.8.8";
    private static final double TEST_LAT = 37.7749;
    private static final double TEST_LON = -122.4194;
    private static final String IP_STACK_KEY = "test_ipstack_key";
    private static final String WEATHER_MAP_KEY = "test_weathermap_key";
    private static final String KELVIN_UNITS = "standard"; // OpenWeatherMap 'standard' for Kelvin
    private static final String CELSIUS_UNITS = "metric";

    @Test
    @DisplayName("Should successfully generate response for valid IP and Kelvin unit")
    void generateResponse_Success_KelvinUnit() throws Exception{


        when(env.getProperty("IP_STACK_API_KEY")).thenReturn(IP_STACK_KEY);
        when(env.getProperty("WEATHER_MAP_API_KEY")).thenReturn(WEATHER_MAP_KEY);

        IpResponse ipResponse = new IpResponse();
        ipResponse.setCity("Mountain View");
        ipResponse.setCountry_name("United States");
        ipResponse.setLatitude(TEST_LAT);
        ipResponse.setLongitude(TEST_LON);

        when(ipClient.getIpAddressDetails(TEST_IP, IP_STACK_KEY)).thenReturn(ipResponse);

        WeatherResponse weatherResponse = new WeatherResponse();
        Main main = new Main();
        main.setTemp(288.15); // Example Kelvin temp (15 C)
        main.setHumidity(60);
        weatherResponse.setMain(main);
        Weather weather = new Weather();
        weather.setDescription("clear sky");
        weatherResponse.setWeather(Collections.singletonList(weather));
        when(weatherClient.getWeatherData(TEST_LAT, TEST_LON, WEATHER_MAP_KEY, KELVIN_UNITS)).thenReturn(weatherResponse);

        IpWeatherResponse response = weatherService.generateResponse(TEST_IP, 'K');

        // Assertions
        assertNotNull(response);
        assertEquals(TEST_IP, response.getIp());
        assertEquals("Mountain View", response.getLocation().getCity());
        assertEquals("United States", response.getLocation().getCountry());
        assertEquals("288.15K", response.getWeather().getTemperature());
        assertEquals(60, response.getWeather().getHumidity());
        assertEquals("clear sky", response.getWeather().getDescription());

        // Verify mocks were called as expected
        verify(ipClient).getIpAddressDetails(TEST_IP, IP_STACK_KEY);
        verify(weatherClient).getWeatherData(TEST_LAT, TEST_LON, WEATHER_MAP_KEY, KELVIN_UNITS);

    }

    @Test
    @DisplayName("Should successfully generate response for valid IP and celsius unit")
    void generateResponse_Success_CelsiusUnit() throws Exception{


        when(env.getProperty("IP_STACK_API_KEY")).thenReturn(IP_STACK_KEY);
        when(env.getProperty("WEATHER_MAP_API_KEY")).thenReturn(WEATHER_MAP_KEY);

        IpResponse ipResponse = new IpResponse();
        ipResponse.setCity("Mountain View");
        ipResponse.setCountry_name("United States");
        ipResponse.setLatitude(TEST_LAT);
        ipResponse.setLongitude(TEST_LON);

        when(ipClient.getIpAddressDetails(TEST_IP, IP_STACK_KEY)).thenReturn(ipResponse);

        WeatherResponse weatherResponse = new WeatherResponse();
        Main main = new Main();
        main.setTemp(15.0); // Example Kelvin temp (15 C)
        main.setHumidity(60);
        weatherResponse.setMain(main);
        Weather weather = new Weather();
        weather.setDescription("clear sky");
        weatherResponse.setWeather(Collections.singletonList(weather));
        when(weatherClient.getWeatherData(TEST_LAT, TEST_LON, WEATHER_MAP_KEY, CELSIUS_UNITS)).thenReturn(weatherResponse);

        IpWeatherResponse response = weatherService.generateResponse(TEST_IP, 'C');

        // Assertions
        assertNotNull(response);
        assertEquals("15.0°C", response.getWeather().getTemperature());

        // Verify mocks were called as expected
        verify(ipClient).getIpAddressDetails(TEST_IP, IP_STACK_KEY);
        verify(weatherClient).getWeatherData(TEST_LAT, TEST_LON, WEATHER_MAP_KEY, CELSIUS_UNITS);

    }

    @Test
    @DisplayName("Should throw InvalidIpAddressException when IP is invalid")
    void generateResponse_InvalidIpAddress_ThrowsException() {

        // Assert that calling generateResponse with an invalid IP throws the expected exception
        InvalidIpAddressException thrown = assertThrows(InvalidIpAddressException.class, () -> {
            weatherService.generateResponse("invalid_ip", 'C');
        });

        // Verify the exception message
        assertEquals("The IP address provided is either empty or invalid", thrown.getMessage());
        assertEquals("Please provide valid ip address", thrown.getSuggestion());
        assertEquals("Client Request Data", thrown.getService());

        // Verify no further calls were made to external clients
        verifyNoInteractions(ipClient);
        verifyNoInteractions(weatherClient);

    }


    @Test
    @DisplayName("Should throw KeyNotFoundException when IpStack API key is missing")
    void generateResponse_IpStackApiKeyMissing_ThrowsException() {
        when(env.getProperty("IP_STACK_API_KEY")).thenReturn(null); // Explicitly set IP_STACK_API_KEY to null
        KeyNotFoundException thrown = assertThrows(KeyNotFoundException.class, () -> {
            weatherService.generateResponse(TEST_IP, 'C');
        });

        assertEquals("Missing IpStack API key.", thrown.getMessage());
        assertEquals("Please provide a valid 'access_key' to authenticate your request.", thrown.getSuggestion());
        assertEquals("Internal Application System.", thrown.getService());

        verifyNoInteractions(ipClient);
        verifyNoInteractions(weatherClient);

    }


    @Test
    @DisplayName("Should throw ServiceUnavailableException when IpClient throws FeignException")
    void generateResponse_IpClientFeignException_ThrowsException() {

        when(env.getProperty("IP_STACK_API_KEY")).thenReturn(IP_STACK_KEY);
        doThrow(mock(FeignException.class)).when(ipClient).getIpAddressDetails(eq(TEST_IP), anyString());

        ServiceUnavailableException thrown = assertThrows(ServiceUnavailableException.class, () -> {
            weatherService.generateResponse(TEST_IP, 'C');
        });

        assertEquals("The IpStack service is currently unreachable or returning errors. Please try again later", thrown.getMessage());
        assertEquals("Please ensure your application's API key (access_key) for IpStack is correct.", thrown.getSuggestion());
        assertEquals("IpStack", thrown.getService());
    }


    @Test
    @DisplayName("Should throw InvalidIpAddressException when IpResponse has zero lat/lon")
    void generateResponse_IpResponseZeroLatLon_ThrowsException() throws Exception {
        when(env.getProperty("IP_STACK_API_KEY")).thenReturn(IP_STACK_KEY);

        IpResponse ipResponse = new IpResponse();
        ipResponse.setSuccess(true);
        ipResponse.setLatitude(0.0); // Zero latitude
        ipResponse.setLongitude(TEST_LON); // Valid longitude
        ipResponse.setCity("Test City");
        ipResponse.setCountry_name("Test Country");
        when(ipClient.getIpAddressDetails(TEST_IP, IP_STACK_KEY)).thenReturn(ipResponse);

        InvalidIpAddressException thrown = assertThrows(InvalidIpAddressException.class, () -> {
            weatherService.generateResponse(TEST_IP, 'C');
        });

        assertEquals("Latitude and/or Longitude value is missing", thrown.getMessage());
        assertEquals("Please provide valid ip address", thrown.getSuggestion());
        assertEquals("Client Request Data", thrown.getService());
        verifyNoInteractions(weatherClient); // Weather client should not be called
    }

    @Test
    @DisplayName("Should throw KeyNotFoundException when OpenWeatherMap API key is missing")
    void generateResponse_WeatherMapApiKeyMissing_ThrowsException() throws Exception {
        when(env.getProperty("IP_STACK_API_KEY")).thenReturn(IP_STACK_KEY);
        when(env.getProperty("WEATHER_MAP_API_KEY")).thenReturn(null); // Explicitly set WEATHER_MAP_API_KEY to null

        // Mock IpClient response for this path
        IpResponse ipResponse = new IpResponse();
        ipResponse.setSuccess(true);
        ipResponse.setLatitude(TEST_LAT);
        ipResponse.setLongitude(TEST_LON);
        when(ipClient.getIpAddressDetails(TEST_IP, IP_STACK_KEY)).thenReturn(ipResponse);

        KeyNotFoundException thrown = assertThrows(KeyNotFoundException.class, () -> {
            weatherService.generateResponse(TEST_IP, 'C');
        });

        assertEquals("Missing OpenWeatherMap API key.", thrown.getMessage());
        assertEquals("Please provide a valid 'appid' to authenticate your request.", thrown.getSuggestion());
        assertEquals("Internal Application System", thrown.getService());
        verify(ipClient).getIpAddressDetails(TEST_IP, IP_STACK_KEY); // IpClient should have been called
        verifyNoInteractions(weatherClient); // Weather client should NOT have been called
    }

    @Test
    @DisplayName("Should throw ServiceUnavailableException when WeatherClient throws FeignException")
    void generateResponse_WeatherClientFeignException_ThrowsException() throws Exception {
        when(env.getProperty("IP_STACK_API_KEY")).thenReturn(IP_STACK_KEY);
        when(env.getProperty("WEATHER_MAP_API_KEY")).thenReturn(WEATHER_MAP_KEY);

        // Mock IpClient response
        IpResponse ipResponse = new IpResponse();
        ipResponse.setSuccess(true);
        ipResponse.setLatitude(TEST_LAT);
        ipResponse.setLongitude(TEST_LON);
        when(ipClient.getIpAddressDetails(TEST_IP, IP_STACK_KEY)).thenReturn(ipResponse);


        // Mock WeatherClient to throw a FeignException
        doThrow(mock(FeignException.class)).when(weatherClient).getWeatherData(eq(TEST_LAT), eq(TEST_LON), anyString(), eq(CELSIUS_UNITS));

        ServiceUnavailableException thrown = assertThrows(ServiceUnavailableException.class, () -> {
            weatherService.generateResponse(TEST_IP, 'C');
        });

        assertEquals("The OpenWeatherMap service is currently unreachable or returning errors. Please try again later", thrown.getMessage());
        assertEquals("Please ensure your application's API key (App ID) for OpenWeatherMap is correct.", thrown.getSuggestion());
        assertEquals("OpenWeatherMap", thrown.getService());

        verify(ipClient).getIpAddressDetails(TEST_IP, IP_STACK_KEY); // IpClient should have been called
    }
}
