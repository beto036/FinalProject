
package com.example.admin.finalproject.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Id {

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
}
