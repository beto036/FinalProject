
package com.example.admin.finalproject.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Friendship implements Parcelable {

    @SerializedName("_id")
    @Expose
    private Id id;
    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("nameSender")
    @Expose
    private String nameSender;
    @SerializedName("lastnameSender")
    @Expose
    private String lastnameSender;
    @SerializedName("emailSender")
    @Expose
    private String emailSender;
    @SerializedName("receiverId")
    @Expose
    private String receiverId;
    @SerializedName("nameReceiver")
    @Expose
    private String nameReceiver;
    @SerializedName("lastnameReceiver")
    @Expose
    private String lastnameReceiver;
    @SerializedName("emailReceiver")
    @Expose
    private String emailReceiver;
    @SerializedName("isRequest")
    @Expose
    private Boolean isRequest;
    @SerializedName("declined")
    @Expose
    private Boolean declined;

    /**
     * 
     * @return
     *     The id
     */
    public Id getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The _id
     */
    public void setId(Id id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The senderId
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * 
     * @param senderId
     *     The senderId
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * 
     * @return
     *     The nameSender
     */
    public String getNameSender() {
        return nameSender;
    }

    /**
     * 
     * @param nameSender
     *     The nameSender
     */
    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    /**
     * 
     * @return
     *     The lastnameSender
     */
    public String getLastnameSender() {
        return lastnameSender;
    }

    /**
     * 
     * @param lastnameSender
     *     The lastnameSender
     */
    public void setLastnameSender(String lastnameSender) {
        this.lastnameSender = lastnameSender;
    }

    /**
     * 
     * @return
     *     The emailSender
     */
    public String getEmailSender() {
        return emailSender;
    }

    /**
     * 
     * @param emailSender
     *     The emailSender
     */
    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * 
     * @return
     *     The receiverId
     */
    public String getReceiverId() {
        return receiverId;
    }

    /**
     * 
     * @param receiverId
     *     The receiverId
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 
     * @return
     *     The nameReceiver
     */
    public String getNameReceiver() {
        return nameReceiver;
    }

    /**
     * 
     * @param nameReceiver
     *     The nameReceiver
     */
    public void setNameReceiver(String nameReceiver) {
        this.nameReceiver = nameReceiver;
    }

    /**
     * 
     * @return
     *     The lastnameReceiver
     */
    public String getLastnameReceiver() {
        return lastnameReceiver;
    }

    /**
     * 
     * @param lastnameReceiver
     *     The lastnameReceiver
     */
    public void setLastnameReceiver(String lastnameReceiver) {
        this.lastnameReceiver = lastnameReceiver;
    }

    /**
     * 
     * @return
     *     The emailReceiver
     */
    public String getEmailReceiver() {
        return emailReceiver;
    }

    /**
     * 
     * @param emailReceiver
     *     The emailReceiver
     */
    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    /**
     * 
     * @return
     *     The isRequest
     */
    public Boolean getIsRequest() {
        return isRequest;
    }

    /**
     * 
     * @param isRequest
     *     The isRequest
     */
    public void setIsRequest(Boolean isRequest) {
        this.isRequest = isRequest;
    }

    public Boolean getDeclined() {
        return declined;
    }

    public void setDeclined(Boolean declined) {
        this.declined = declined;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", senderId='" + senderId + '\'' +
                ", nameSender='" + nameSender + '\'' +
                ", lastnameSender='" + lastnameSender + '\'' +
                ", emailSender='" + emailSender + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", nameReceiver='" + nameReceiver + '\'' +
                ", lastnameReceiver='" + lastnameReceiver + '\'' +
                ", emailReceiver='" + emailReceiver + '\'' +
                ", isRequest=" + isRequest +
                ", declined=" + declined +
                '}';
    }

    public Friendship(){}

    protected Friendship(Parcel in) {
        id = (Id) in.readValue(Id.class.getClassLoader());
        senderId = in.readString();
        nameSender = in.readString();
        lastnameSender = in.readString();
        emailSender = in.readString();
        receiverId = in.readString();
        nameReceiver = in.readString();
        lastnameReceiver = in.readString();
        emailReceiver = in.readString();
        byte isRequestVal = in.readByte();
        isRequest = isRequestVal == 0x02 ? null : isRequestVal != 0x00;
        byte declinedVal = in.readByte();
        declined = declinedVal == 0x02 ? null : declinedVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(senderId);
        dest.writeString(nameSender);
        dest.writeString(lastnameSender);
        dest.writeString(emailSender);
        dest.writeString(receiverId);
        dest.writeString(nameReceiver);
        dest.writeString(lastnameReceiver);
        dest.writeString(emailReceiver);
        if (isRequest == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isRequest ? 0x01 : 0x00));
        }
        if (declined == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (declined ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Friendship> CREATOR = new Parcelable.Creator<Friendship>() {
        @Override
        public Friendship createFromParcel(Parcel in) {
            return new Friendship(in);
        }

        @Override
        public Friendship[] newArray(int size) {
            return new Friendship[size];
        }
    };
}
