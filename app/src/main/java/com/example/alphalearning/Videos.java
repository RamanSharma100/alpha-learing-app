package com.example.alphalearning;

import java.util.Date;

public class Videos {

    private String videoUrl,videoTitle,courseId,videoBy;
    private Date createdAt,updatedAt;

    public Videos(){}

    public Videos(String videoUrl, String videoTitle, String courseId, String videoBy, Date createdAt, Date updatedAt) {
        this.videoUrl = videoUrl;
        this.videoTitle = videoTitle;
        this.courseId = courseId;
        this.videoBy = videoBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getVideoBy() {
        return videoBy;
    }

    public void setVideoBy(String videoBy) {
        this.videoBy = videoBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }




}
