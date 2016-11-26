
package com.example.admin.finalproject.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("latitude")
    @Expose
    private Integer latitude;
    @SerializedName("longitude")
    @Expose
    private Integer longitude;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("isAdmin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("date")
    @Expose
    private Date date;

    /**
     * 
     * @return
     *     The event
     */
    public String getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     *     The event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public Integer getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    public Integer getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The isAdmin
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * 
     * @param isAdmin
     *     The isAdmin
     */
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 
     * @return
     *     The date
     */
    public Date getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Event{" +
                "event='" + event + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", userId='" + userId + '\'' +
                ", isAdmin=" + isAdmin +
                ", date=" + date +
                '}';
    }
}
