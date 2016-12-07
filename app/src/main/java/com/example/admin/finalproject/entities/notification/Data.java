
package com.example.admin.finalproject.entities.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("time")
    @Expose
    private String time;

    /**
     * 
     * @return
     *     The action
     */
    public String getAction() {
        return action;
    }

    /**
     * 
     * @param action
     *     The action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 
     * @return
     *     The time
     */
    public String getTime() {
        return time;
    }

    /**
     * 
     * @param time
     *     The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Data{" +
                "action='" + action + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
