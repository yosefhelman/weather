package com.example.wow2.weather;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.widget.ListView;

public class MainActivity extends AppCompatActivity {


    private static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast/";
    private static final String API_KEY = "34f46c39044df7749e215348898875f6";
    private String cityID="293198";
    private String unit="metric";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if ( activeNetworkInfo!= null){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    getWeatherData();
                    handler.sendEmptyMessage(0);
                }
            };
            Thread thread=new Thread(runnable);
            thread.start();
        }
        else Log.e("tag","no internet");

    }



    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            updateWeatherDisplay();
        }
    };

    public void getWeatherData()
    {
        WeatherDBHandler dbHandler=new WeatherDBHandler(this,null,null,1);
        String threadError="";
        String s="";
        JSONObject rowData;

        try {
            URL url = new URL(API_URL + "city?id=" + cityID + "&APPID=" + API_KEY+"&units="+unit);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                s=stringBuilder.toString();
            } finally {
                urlConnection.disconnect();

            }
        }
        catch (Exception e)
        {
            threadError+=" fetching error";
        }

        try
        {
            JSONObject json = new JSONObject( s );
            threadError +="1";
            JSONArray forecastJArr=json.getJSONArray("list");
            threadError +="2";
            JSONObject singleForecast;
            JSONObject singleMain;
            SQLForecast temp=new SQLForecast();

            threadError +="3";

            for (int i=0;i<forecastJArr.length();i++)
            {
                singleForecast=forecastJArr.getJSONObject(i);
                threadError+="4";

                temp.set_id(singleForecast.getInt("dt"));
                threadError+="4.5";
                singleMain=singleForecast.getJSONObject("main");
                threadError+="5";
                temp.set_min_temp(singleMain.getInt("temp_min"));
                threadError+="6";
                temp.set_max_temp(singleMain.getInt("temp_max"));
                threadError+="7";

                temp.set_weatherType(singleForecast.getJSONArray("weather").getJSONObject(0).getString("description"));
                threadError+="8";

                Log.i("tag","before add: "+temp.toString());

                if (dbHandler.exists(temp.get_id()))
                    dbHandler.updateRow(temp);
                else dbHandler.AddRow(temp);

            }
        }
        catch (JSONException e)
        {
            threadError+=" JASON Error";
        }

        Log.i("tag","DB before delete out dated : "+dbHandler.toString());


        Calendar yesterday=Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        yesterday.set(Calendar.HOUR_OF_DAY,0);
        //current.set(Calendar.MINUTE,0);
        //current.set(Calendar.SECOND,0);

        yesterday.add(Calendar.DAY_OF_MONTH,-1);
        dbHandler.DeleteBefore((int)(yesterday.getTimeInMillis()/1000));
    }

    public void updateWeatherDisplay()
    {

        TextView today_date_text=(TextView)findViewById(R.id.today_date);
        TextView today_max_text=(TextView)findViewById(R.id.today_max);
        TextView today_min_text=(TextView)findViewById(R.id.today_min);
        TextView today_type=(TextView)findViewById(R.id.today_type);
        ImageView today_whether_icon=(ImageView)findViewById(R.id.today_weather_icon);
        WeatherDBHandler dbHandler=new WeatherDBHandler(this,null,null,1);
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd:MM:yyyy");
        ListView listView=(ListView)findViewById(R.id.listView);

        Calendar today=Calendar.getInstance();
        dateFormat.setTimeZone(TimeZone.getDefault());
        today_date_text.setText(dateFormat.format(today.getTime()));

        today.set(Calendar.HOUR_OF_DAY,0);//move it to the beginning of the day

        Calendar todayEnd=Calendar.getInstance();
        //maybe change time to early next morning?
        todayEnd.add(Calendar.DAY_OF_MONTH,1);


        //set today's fields:
        int maxTemp=dbHandler.getMaxTemp((int)(today.getTimeInMillis()/1000),(int)(todayEnd.getTimeInMillis()/1000));
        int minTemp=dbHandler.getMinTemp((int)(today.getTimeInMillis()/1000),(int)(todayEnd.getTimeInMillis()/1000));
        today_max_text.setText(String.valueOf(maxTemp));
        today_min_text.setText(String.valueOf(minTemp));
        try {
                    today_type.setText(
                            dbHandler.GetType((int) (today.getTimeInMillis() / 1000), (int) (todayEnd.getTimeInMillis() / 1000)));
        }catch (InvalidWeatherTypeException e){
            //say "there was an error try refreshing "
            Log.e("error53706583768","Invalid Weather Type Exception: "+e.getMessage());

        }

        //get daily forecast
        SQLForecast[] forecasts=new SQLForecast[5];

        for(int i=0;i<5;i++)
        {
            today.add(Calendar.DAY_OF_MONTH,1);
            todayEnd.add(Calendar.DAY_OF_MONTH,1);
            forecasts[i]=new SQLForecast();

            forecasts[i].set_id((int)(today.getTimeInMillis()/1000));
            forecasts[i].set_max_temp(
                    dbHandler.getMaxTemp((int)(today.getTimeInMillis()/1000),(int)(todayEnd.getTimeInMillis()/1000)));
            forecasts[i].set_min_temp(
                    dbHandler.getMinTemp((int)(today.getTimeInMillis()/1000),(int)(todayEnd.getTimeInMillis()/1000)));
            try {
                forecasts[i].set_weatherType(
                        dbHandler.GetType((int) (today.getTimeInMillis() / 1000), (int) (todayEnd.getTimeInMillis() / 1000)));
            }catch (InvalidWeatherTypeException e){
                //say "there was an error try refreshing "
                Log.e("error","Invalid Weather Type Exception: "+e.getMessage());

            }
        }


        dayAdapter dayAdapter =new dayAdapter(this,forecasts);
        listView.setAdapter(dayAdapter);


        Log.i("tag","DB at end : "+dbHandler.toString());

    }


}


