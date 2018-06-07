package com.example.myutar;

/**
 * Created by limrusin on 19/1/2018.
 */

public class FeedbackResponse {
    private String date, response, departmentID;

    public FeedbackResponse(String date, String response, String departmentID) {
        this.date = date;
        this.response = response;
        this.departmentID = departmentID;
    }

    public String getDate() {
        return date;
    }

    public String getResponse() {
        return response;
    }

    public String getDepartmentID() {
        return departmentID;
    }
}
