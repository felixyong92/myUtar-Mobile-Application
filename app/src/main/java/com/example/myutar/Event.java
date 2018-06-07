package com.example.myutar;

/**
 * Created by limrusin on 19/1/2018.
 */

public class Event {

    private String departmentID, title, description, type, expiryDate;
    private String[] image, attachment;

    public Event(String departmentID, String title, String description, String[] image, String[] attachment, String type, String expiryDate) {
        this.departmentID = departmentID;
        this.title = title;
        this.description = description;
        this.image = image;
        this.attachment = attachment;
        this.type = type;
        this.expiryDate = expiryDate;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getImage() {
        return image;
    }

    public String[] getAttachment() {
        return attachment;
    }

    public String getType() {
        return type;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
