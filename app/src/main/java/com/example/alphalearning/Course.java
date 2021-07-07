package com.example.alphalearning;

import java.sql.Timestamp;

public class Course {
    private String category, createdBy, name, thumbnail;
    private String[] enrolledBy, videos;
    private int students;
    private Timestamp updatedAt, createdAt;

    public Course() {
    }

    public Course(String category, String createdBy, String name, String thumbnail, String[] enrolledBy, String[] videos, int students, Timestamp updatedAt, Timestamp createdAt) {
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

    public String[] getEnrolledBy() {
        return enrolledBy;
    }

    public void setEnrolledBy(String[] enrolledBy) {
        this.enrolledBy = enrolledBy;
    }

    public String[] getVideos() {
        return videos;
    }

    public void setVideos(String[] videos) {
        this.videos = videos;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
