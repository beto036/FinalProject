
package com.example.admin.finalproject.entities.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SendNotificationResponse {

    @SerializedName("multicast_id")
    @Expose
    private Double multicastId;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("failure")
    @Expose
    private Integer failure;
    @SerializedName("canonical_ids")
    @Expose
    private Integer canonicalIds;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();

    /**
     * 
     * @return
     *     The multicastId
     */
    public Double getMulticastId() {
        return multicastId;
    }

    /**
     * 
     * @param multicastId
     *     The multicast_id
     */
    public void setMulticastId(Double multicastId) {
        this.multicastId = multicastId;
    }

    /**
     * 
     * @return
     *     The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     * 
     * @return
     *     The failure
     */
    public Integer getFailure() {
        return failure;
    }

    /**
     * 
     * @param failure
     *     The failure
     */
    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    /**
     * 
     * @return
     *     The canonicalIds
     */
    public Integer getCanonicalIds() {
        return canonicalIds;
    }

    /**
     * 
     * @param canonicalIds
     *     The canonical_ids
     */
    public void setCanonicalIds(Integer canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "SendNotificationResponse{" +
                "multicastId=" + multicastId +
                ", success=" + success +
                ", failure=" + failure +
                ", canonicalIds=" + canonicalIds +
                ", results=" + results +
                '}';
    }
}
