package com.example.calorie.exception;

public class LogEntryNotFoundException extends RuntimeException{

    public LogEntryNotFoundException(String message){
        super(message);
    }
}
