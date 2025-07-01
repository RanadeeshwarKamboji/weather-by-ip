package com.rana.weather_by_ip.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomError {
    private int status;
    private String error;
    private String message;
    private Details details;
    private String path;
    private LocalDateTime timestamp;

    public CustomError(int status, String error, String message, String path, Details details) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

}
