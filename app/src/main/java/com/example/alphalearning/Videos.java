package com.example.alphalearning;

import java.sql.Timestamp;

public class Videos {

    private String videoUrl,videoTitle,courseId,videoBy;
    private Timestamp createdAt,updatedAt;

    public Videos(){}

    public Videos(String videoUrl, String videoTitle, String courseId, String videoBy, Timestamp createdAt, Timestamp updatedAt) {
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }




}
