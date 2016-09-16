package com.example.wow2.weather;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;


public class WeatherDBHandler extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="Weather.db";//must have .db
    public static final String TABLE_NAME="Weather";//can have a few in one db

    //columns
    public static final String COLUMNS_ID="_id";
    public static final String COLUMNS_MAX="_max";
    public static final String COLUMNS_MIN="_min";
    public static final String COLUMNS_WEATHER="_weather";

    //constructor
    public WeatherDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        SQLiteDatabase db=getWritableDatabase();
        db.close();
    }

    //creating table
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Log.e("tag","on create called");
        String query="CREATE TABLE " + TABLE_NAME + "(" +
                COLUMNS_ID + " INTEGER PRIMARY KEY, " +
                COLUMNS_MAX +" INTEGER, "+
                COLUMNS_MIN +" INTEGER, "+
                COLUMNS_WEATHER+" TEXT "+
                " ) ; ";
        db.execSQL(query);
        //Log.e("tag","on create done");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void AddRow(SQLForecast forecast)
    {
        Log.i("tag","addRow called");
        ContentValues values=new ContentValues();
        values.put(COLUMNS_ID, forecast.get_id());
        values.put(COLUMNS_MAX,forecast.get_max_temp());
        values.put(COLUMNS_MIN,forecast.get_min_temp());
        values.put(COLUMNS_WEATHER,forecast.get_weatherType());
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void DeleteRow(int dt)
    {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+COLUMNS_ID+"=\""+dt+"\";");
        db.close();
    }

    public void DeleteBefore(int dt)
    {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+COLUMNS_ID+"<\""+dt+"\";");//might not need "" on dt?
        db.close();
    }

    public void updateRow(SQLForecast f)
    {
        Log.i("tag","updateRow called");
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NAME+" SET "+
                COLUMNS_MAX+ "=" +f.get_max_temp()+", "+
                COLUMNS_MIN+ "=" +f.get_min_temp()+", "+
                COLUMNS_WEATHER+ "=\"" +f.get_weatherType()+
                "\" WHERE "+COLUMNS_ID+"="+f.get_id()+";");
        db.close();

    }

    public int getMaxTemp(int from,int to) {
        SQLiteDatabase db=getReadableDatabase();
        String query=" SELECT MAX ("+COLUMNS_MAX+") FROM "+TABLE_NAME+" WHERE "+
                COLUMNS_ID+">="+from+" AND "
                +COLUMNS_ID+"<="+to;
        Cursor c=db.rawQuery(query,null);

        Log.i("tag","index "+c.getColumnIndex(COLUMNS_MAX));
        c.moveToFirst();
        int max= c.getInt(0);
        db.close();
        return max;
    }

    public int getMinTemp(int from,int to) {
        SQLiteDatabase db=getReadableDatabase();
        String query=" SELECT MIN ("+COLUMNS_MIN+") FROM "+TABLE_NAME+" WHERE "+
                COLUMNS_ID+">="+from+" AND "
                +COLUMNS_ID+"<="+to;
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        int min= c.getInt(0);
        db.close();
        return min;
    }

    public String GetType(int from,int to) throws InvalidWeatherTypeException
    {
        SQLiteDatabase db=getReadableDatabase();

        String query=" SELECT "+COLUMNS_MIN+" FROM "+TABLE_NAME+" WHERE "+
                COLUMNS_ID+">="+from+" AND "
                +COLUMNS_ID+"<="+to;
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        WeatherTypes weatherTypes=new WeatherTypes();


            while (!c.isAfterLast()){
                Log.i("tag",c.getString(0));
                weatherTypes.add(c.getString(0));
            }

        db.close();
        return weatherTypes.getWeatherType();

    }



    public boolean exists(int x){
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT "+COLUMNS_ID+" FROM "+TABLE_NAME+" WHERE "+COLUMNS_ID+"="+x+";";
        Cursor c=db.rawQuery(query,null);
        if (c.getCount()<=0) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public String toString(){
        String dbString="";
        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE 1";
        //CURSoR
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast())
        {
            if (c.getString(c.getColumnIndex(COLUMNS_MAX))!=null)
                dbString+=c.getInt(c.getColumnIndex(COLUMNS_ID));
            dbString+="\n";
            c.move(1);
        }
        db.close();
        return dbString;
    }



}
