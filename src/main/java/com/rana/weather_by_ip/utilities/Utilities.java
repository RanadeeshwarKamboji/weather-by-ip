package com.rana.weather_by_ip.utilities;

import com.rana.weather_by_ip.exceptions.InvalidIpAddressException;
import org.apache.commons.validator.routines.InetAddressValidator;

public class Utilities {
    public static boolean isValidIp(String ip) throws InvalidIpAddressException{
        if(ip !=null) {
            InetAddressValidator validator = InetAddressValidator.getInstance();
            return validator.isValid(ip);// Validates both IPv4 and IPv6
        }
        return false;
    }
    public static String getUnits(char unit){
        if(unit == 'K'){
            return "standard";
        }
        else if(unit == 'F'){
            return "imperial";
        }
        else{
            return "metric";
        }
    }
}
