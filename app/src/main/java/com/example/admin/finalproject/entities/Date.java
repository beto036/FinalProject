
package com.example.admin.finalproject.entities;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date {

    private static final String TAG = "DateTAG_";
    @SerializedName("$date")
    @Expose
    private String $date;

    public Date(){}

    public Date(String date) {
        $date = date;
    }


    /**
     * 
     * @return
     *     The $date
     */
    public String get$date() {
        return $date;
    }

    /**
     * 
     * @param $date
     *     The $date
     */
    public void set$date(String $date){
        this.$date = $date;
    }

    @Override
    public String toString() {
        return "Date{" +
                "$date='" + $date + '\'' +
                '}';
    }
}
