
package com.example.admin.finalproject.entities.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendNotificationRequest {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("notification")
    @Expose
    private Notification notification;

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     *
     * @return
     *     The notification
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     *
     * @param notification
     *     The notification
     */
    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    /**
     * 
     * @return
     *     The to
     */
    public String getTo() {
        return to;
    }

    /**
     * 
     * @param to
     *     The to
     */
    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "SendNotificationRequest{" +
                "data=" + data +
                ", to='" + to + '\'' +
                ", notification=" + notification +
                '}';
    }
}
