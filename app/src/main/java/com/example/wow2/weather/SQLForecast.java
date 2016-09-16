package com.example.wow2.weather;


import java.util.Date;

public class SQLForecast {

    private int _id;
    private int _max_temp;
    private int _min_temp;
    private String _weatherType;

    public SQLForecast()
    {

    }

    //get

    public int get_id() {
        return _id;
    }

    public int get_max_temp() {
        return _max_temp;
    }

    public int get_min_temp() {
        return _min_temp;
    }

    public String get_weatherType() {
        return _weatherType;
    }

    //set


    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_max_temp(int _max_temp) {
        this._max_temp = _max_temp;
    }

    public void set_min_temp(int _min_temp) {
        this._min_temp = _min_temp;
    }

    public void set_weatherType(String _weatherType) {
        this._weatherType = _weatherType;
    }

    @Override
    public String toString() {
        return "SQLForecast{" +
                "_id=" + _id +
                ", _max=" + _max_temp +
                ", _min=" + _min_temp +
                ", _Type='" + _weatherType + '\'' +
                '}';
    }
}
