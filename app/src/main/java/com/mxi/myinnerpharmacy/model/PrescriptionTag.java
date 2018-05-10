package com.mxi.myinnerpharmacy.model;

/**
 * Created by android on 20/1/17.
 */
public class PrescriptionTag {
    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getPrescription_name() {
        return prescription_name;
    }

    public void setPrescription_name(String prescription_name) {
        this.prescription_name = prescription_name;
    }

    String tag_id,prescription_name;
}
