package com.example.wow2.weather;


import android.os.Message;

public class InvalidWeatherTypeException extends Exception
{
    public InvalidWeatherTypeException(String message){
        super(message);
    }

    public String getMessage()
    {
        return super.getMessage();
    }
}
