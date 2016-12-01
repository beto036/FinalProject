
package com.example.admin.finalproject.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Id implements Parcelable{

    @SerializedName("$oid")
    @Expose
    private String $oid;

    /**
     * 
     * @return
     *     The $oid
     */
    public String get$oid() {
        return $oid;
    }

    /**
     * 
     * @param $oid
     *     The $oid
     */
    public void set$oid(String $oid) {
        this.$oid = $oid;
    }

    @Override
    public String toString() {
        return "Id{" +
                "$oid='" + $oid + '\'' +
                '}';
    }

    protected Id(Parcel in) {
        $oid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString($oid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Id> CREATOR = new Parcelable.Creator<Id>() {
        @Override
        public Id createFromParcel(Parcel in) {
            return new Id(in);
        }

        @Override
        public Id[] newArray(int size) {
            return new Id[size];
        }
    };
}
