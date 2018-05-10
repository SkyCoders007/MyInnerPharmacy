package com.mxi.myinnerpharmacy.model;

/**
 * Created by android on 21/1/17.
 */
public class PrescriptionDetails {


    String media_type;
    String media_file;
    String media_name;
    String media_id;
    String prescription_videopath;

    public String getPrescription_videopath() {
        return prescription_videopath;
    }

    public void setPrescription_videopath(String prescription_videopath) {
        this.prescription_videopath = prescription_videopath;
    }

    public String getPrescription_audiopath() {
        return prescription_audiopath;
    }

    public void setPrescription_audiopath(String prescription_audiopath) {
        this.prescription_audiopath = prescription_audiopath;
    }

    public String getPrescription_textpath() {
        return prescription_textpath;
    }

    public void setPrescription_textpath(String prescription_textpath) {
        this.prescription_textpath = prescription_textpath;
    }

    String prescription_audiopath;
    String prescription_textpath;

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_file() {
        return media_file;
    }

    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }


}
