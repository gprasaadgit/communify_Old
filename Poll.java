package com.gap22.community.apartment.Database;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Shiva on 3/18/17.
 */

public class Poll {




   private HashMap<String,Object> Option1;

    public HashMap<String, Object> getOption1() {
        return Option1;
    }

    public void setOption1(HashMap<String, Object> option1) {
        Option1 = option1;
    }

    private HashMap<String,Object> Option2;

    public HashMap<String, Object> getOption2() {
        return Option2;
    }

    public void setOption2(HashMap<String, Object> option2) {
        Option2 = option2;
    }

    public HashMap<String, Object> getOption3() {
        return Option3;
    }

    public void setOption3(HashMap<String, Object> option3) {
        Option3 = option3;
    }

    private HashMap<String,Object> Option3;

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String Question;
    private String status;
}
