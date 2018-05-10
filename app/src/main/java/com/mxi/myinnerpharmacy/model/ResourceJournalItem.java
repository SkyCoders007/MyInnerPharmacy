package com.mxi.myinnerpharmacy.model;

/**
 * Created by android on 19/1/17.
 */
public class ResourceJournalItem {
    String title;
    String description;
    String audio_file_path;
    boolean fromMydeads;

    public boolean isFromMydeads() {
        return fromMydeads;
    }

    public void setFromMydeads(boolean fromMydeads) {
        this.fromMydeads = fromMydeads;
    }

    public String getAudio_file_path() {
        return audio_file_path;
    }

    public void setAudio_file_path(String audio_file_path) {
        this.audio_file_path = audio_file_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
