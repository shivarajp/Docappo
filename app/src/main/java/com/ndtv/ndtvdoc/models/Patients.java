package com.ndtv.ndtvdoc.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 4/8/2016.
 */

public class Patients implements Serializable {

    @SerializedName("patients")
    @Expose
    private List<Patient> patients = new ArrayList<Patient>();

    /**
     *
     * @return
     * The patients
     */
    public List<Patient> getPatients() {
        return patients;
    }

    /**
     *
     * @param patients
     * The patients
     */
    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

}
