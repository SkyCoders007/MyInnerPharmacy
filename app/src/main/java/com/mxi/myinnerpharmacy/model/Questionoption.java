package com.mxi.myinnerpharmacy.model;

/**
 * Created by sonali on 3/1/17.
 */
public class Questionoption {
    public int checkedId = -1;
    public Boolean checked = false;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String text;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    String answer;

    public int getNumOfAnswerOption() {
        return numOfAnswerOption;
    }

    public void setNumOfAnswerOption(int numOfAnswerOption) {
        this.numOfAnswerOption = numOfAnswerOption;
    }

    int numOfAnswerOption;
}
