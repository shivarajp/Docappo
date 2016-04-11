package com.ndtv.ndtvdoc.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 4/8/2016.
 */


public class PatientDetails implements Serializable {

    @SerializedName("patientDetails")
    @Expose
    private List<PatientDetail> patientDetails = new ArrayList<PatientDetail>();

    /**
     * @return The patientDetails
     */
    public List<PatientDetail> getPatientDetails() {
        return patientDetails;
    }

    /**
     * @param patientDetails The patientDetails
     */
    public void setPatientDetails(List<PatientDetail> patientDetails) {
        this.patientDetails = patientDetails;
    }



}

