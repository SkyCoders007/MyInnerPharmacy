package com.mxi.myinnerpharmacy.model;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by sonali on 4/1/17.
 */
@Table
public class Firstregistrationstep extends SugarRecord {
    String question_achieve;
    String question_level;
    String achieve_image;
    String current_image;
    String state_colibration;
    String heart_ratemonitor;
    String breathing_analysis;
    String pause_task;
    String backup_time;

    public String getSleeping_time() {
        return sleeping_time;
    }

    public void setSleeping_time(String sleeping_time) {
        this.sleeping_time = sleeping_time;
    }

    public String getBackup_time() {
        return backup_time;
    }

    public void setBackup_time(String backup_time) {
        this.backup_time = backup_time;
    }

    String sleeping_time;

    public String getQuestion_achieve() {
        return question_achieve;
    }

    public void setQuestion_achieve(String question_achieve) {
        this.question_achieve = question_achieve;
    }

    public String getQuestion_level() {
        return question_level;
    }

    public void setQuestion_level(String question_level) {
        this.question_level = question_level;
    }

    public String getAchieve_image() {
        return achieve_image;
    }

    public void setAchieve_image(String achieve_image) {
        this.achieve_image = achieve_image;
    }

    public String getCurrent_image() {
        return current_image;
    }

    public void setCurrent_image(String current_image) {
        this.current_image = current_image;
    }

    public String getState_colibration() {
        return state_colibration;
    }

    public void setState_colibration(String state_colibration) {
        this.state_colibration = state_colibration;
    }

    public String getHeart_ratemonitor() {
        return heart_ratemonitor;
    }

    public void setHeart_ratemonitor(String heart_ratemonitor) {
        this.heart_ratemonitor = heart_ratemonitor;
    }

    public String getBreathing_analysis() {
        return breathing_analysis;
    }

    public void setBreathing_analysis(String breathing_analysis) {
        this.breathing_analysis = breathing_analysis;
    }

    public String getPause_task() {
        return pause_task;
    }

    public void setPause_task(String pause_task) {
        this.pause_task = pause_task;
    }


}
