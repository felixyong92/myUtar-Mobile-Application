package com.example.myutar;

/**
 * Created by limrusin on 19/1/2018.
 */

public class Notification {

    private String departmentID, title, description, link, publishDate;
    private String[] image, attachment;

    public Notification(String departmentID, String title, String description, String[] image, String[] attachment, String link, String publishDate) {
        this.departmentID = departmentID;
        this.title = title;
        this.description = description;
        this.image = image;
        this.attachment = attachment;
        this.link = link;
        this.publishDate = publishDate;
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

    public String getLink() {
        return link;
    }

    public String getPublishDate() {
        return publishDate;
    }
}
