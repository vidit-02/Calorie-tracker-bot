package com.example.calorie.exception;

public class DailyLogNotFoundException extends RuntimeException {

    public DailyLogNotFoundException(String message){
        super(message);
    }

}
