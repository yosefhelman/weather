package com.example.wow2.weather;


import java.util.Collections;
import java.util.HashMap;

public class WeatherTypes {

    private int clear =0;
    private int few_clouds=0;
    private int partly_cloudy =0;
    private int overcast =0;
    private int drizzle =0;
    private int rain=0;
    private int thunderstorm=0;
    private int snow=0;
    private int mist=0;

    private HashMap<String,Integer> map=new HashMap<>(9);

    public WeatherTypes(){
        map.put("clear",0);
        map.put("few_clouds",0);
        map.put("partly_cloudy",0);
        map.put("overcast",0);
        map.put("drizzle",0);
        map.put("rain",0);
        map.put("thunderstorm",0);
        map.put("snow",0);
        map.put("mist",0);
    };

    public void add(String x)throws InvalidWeatherTypeException
    {
        switch (x){
            case "clear sky": this.map.put("clear",this.map.get("clear"));
                break;
            case "few clouds": this.map.put("few_clouds",this.map.get("few_clouds"));
                break;
            case "scattered clouds": this.map.put("partly_cloudy",this.map.get("partly_cloudy"));
                break;
            case "broken clouds": this.map.put("overcast",this.map.get("overcast"));
                break;
            case "shower rain": this.map.put("drizzle",this.map.get("drizzle"));
                break;
            case "rain": this.map.put("rain",this.map.get("rain"));
                break;
            case "thunderstorm": this.map.put("thunderstorm",this.map.get("thunderstorm"));
                break;
            case "snow": this.map.put("snow",this.map.get("snow"));
                break;
            case "mist": this.map.put("mist",this.map.get("mist"));
                break;
            default: throw new InvalidWeatherTypeException(x);
        }
    }

    public String getWeatherType()
    {
        if (this.map.get("snow")>0)
            return "snow";
        if (this.map.get("thunderstorm")>3)
            return "thunderstorm";

        int max= Collections.max(this.map.values());

        if(this.map.get("thunderstorm")==max)
            return "thunderstorm";
        if(this.map.get("rain")==max)
            return "rain";
        if(this.map.get("mist")==max)
            return "mist";
        if(this.map.get("drizzle")==max)
            return "drizzle";
        if(this.map.get("overcast")==max)
            return "overcast";
        if(this.map.get("partly_cloudy")==max)
            return "partly_cloudy";
        if(this.map.get("few_clouds")==max)
            return "few_clouds";

        //if(this.map.get("clear")==max)
            return "clear";

        //throw new Exception("invalid Weather type");

    }




}
