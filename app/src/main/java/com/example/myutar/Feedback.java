package com.example.myutar;

/**
 * Created by limrusin on 19/1/2018.
 */

public class Feedback {

    private int id, type, status;
    private String contents, date, departmentID;
    private String[] attachment;

    public Feedback(int id, int type, String contents, String[] attachment, String date, int status, String departmentID) {
        this.id = id;
        this.type = type;
        this.contents = contents;
        this.attachment = attachment;
        this.date = date;
        this.status = status;
        this.departmentID = departmentID;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getContents() {
        return contents;
    }

    public String[] getAttachment() {
        return attachment;
    }

    public  String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public String getDepartmentID() {
        return departmentID;
    }
}
