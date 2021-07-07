package com.example.alphalearning;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Course {
    private String category, createdBy, name, thumbnail;
    private List<String> enrolledBy, videos;
    private int students;
    private Date updatedAt, createdAt;

    public Course() {
    }

    public Course(String category, String createdBy, String name, String thumbnail, List<String> enrolledBy, List<String> videos, int students, Date updatedAt, Date createdAt) {
        this.category = category;
        this.createdBy = createdBy;
        this.name = name;
        this.thumbnail = thumbnail;
        this.enrolledBy = enrolledBy;
        this.videos = videos;
        this.students = students;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getEnrolledBy() {
        return enrolledBy;
    }

    public void setEnrolledBy(List<String> enrolledBy) {
        this.enrolledBy = enrolledBy;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
