package com.rana.weather_by_ip;
import com.rana.weather_by_ip.clients.IpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.rana.weather_by_ip.utilities.Utilities.getUnits;
import static com.rana.weather_by_ip.utilities.Utilities.isValidIp;
import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void testUnitsMethod(){
        String kResult = getUnits('K');
        String cResult = getUnits('C');
        String fResult = getUnits('F');
        assertEquals("standard", kResult, "The result should be 'standard'");
        assertEquals("metric", cResult, "The result should be 'metric'");
        assertEquals("imperial", fResult, "The result should be 'imperial'");
    }

    @Test
    void testIpValidation(){
        boolean firstResult = isValidIp("127.0.0.1");
        boolean secondResult = isValidIp("999.999.999.999");
        boolean thirdResult = isValidIp("123.123.123.123");
        boolean fourthResult = isValidIp("2001:4860:4860::8888");

        assertTrue(firstResult);
        assertFalse(secondResult);
        assertTrue(thirdResult);
        assertTrue(fourthResult);
    }
}
