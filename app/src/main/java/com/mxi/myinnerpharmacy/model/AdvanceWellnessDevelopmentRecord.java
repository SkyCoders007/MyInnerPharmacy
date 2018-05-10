package com.mxi.myinnerpharmacy.model;

import com.orm.SugarRecord;


/**
 * Created by android on 31/1/17.
 */

public class AdvanceWellnessDevelopmentRecord extends SugarRecord {

    String hours,sugary,play,exercise,stillness,tv,computer,smart_phone,portion,portions;
    String date;


    public AdvanceWellnessDevelopmentRecord(){
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getSugary() {
        return sugary;
    }

    public void setSugary(String sugary) {
        this.sugary = sugary;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getStillness() {
        return stillness;
    }

    public void setStillness(String stillness) {
        this.stillness = stillness;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    public String getComputer() {
        return computer;
    }

    public void setComputer(String computer) {
        this.computer = computer;
    }

    public String getSmart_phone() {
        return smart_phone;
    }

    public void setSmart_phone(String smart_phone) {
        this.smart_phone = smart_phone;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }
}
