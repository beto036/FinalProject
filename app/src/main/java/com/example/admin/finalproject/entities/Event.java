
package com.example.admin.finalproject.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event implements Parcelable{

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

    protected Event(Parcel in) {
        event = in.readString();
        description = in.readString();
        latitude = in.readByte() == 0x00 ? null : in.readInt();
        longitude = in.readByte() == 0x00 ? null : in.readInt();
        userId = in.readString();
        byte isAdminVal = in.readByte();
        isAdmin = isAdminVal == 0x02 ? null : isAdminVal != 0x00;
        long tmpDate = in.readLong();
        date = new Date(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeString(description);
        if (latitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(longitude);
        }
        dest.writeString(userId);
        if (isAdmin == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isAdmin ? 0x01 : 0x00));
        }
        dest.writeString(date.get$date());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
