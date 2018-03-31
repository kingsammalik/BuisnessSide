package com.e.puja.buisnessside.Response;

/**
 * Created by sachinmalik on 31/03/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("packed_on")
    @Expose
    private String packedOn;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("processor")
    @Expose
    private String processor;
    @SerializedName("distributor")
    @Expose
    private Object distributor;
    @SerializedName("sent_by_processor")
    @Expose
    private String sentByProcessor;
    @SerializedName("received_by_distributor")
    @Expose
    private Object receivedByDistributor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPackedOn() {
        return packedOn;
    }

    public void setPackedOn(String packedOn) {
        this.packedOn = packedOn;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public Object getDistributor() {
        return distributor;
    }

    public void setDistributor(Object distributor) {
        this.distributor = distributor;
    }

    public String getSentByProcessor() {
        return sentByProcessor;
    }

    public void setSentByProcessor(String sentByProcessor) {
        this.sentByProcessor = sentByProcessor;
    }

    public Object getReceivedByDistributor() {
        return receivedByDistributor;
    }

    public void setReceivedByDistributor(Object receivedByDistributor) {
        this.receivedByDistributor = receivedByDistributor;
    }

}