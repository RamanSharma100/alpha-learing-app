package com.example.alphalearning;

import java.sql.Timestamp;

public class User {

    private String name, email, uid, currentVideoId, currentCourseId, currentVideoDuration, currentVideoIndex;
    private boolean instructor;
    private String[] createdCourses, enrolledCourses, startedCourses;
    private int students;
    private Timestamp createdAt;

    public User() {
    }

    public User(String name, String email, String uid, String currentVideoId, String currentCourseId, String currentVideoDuration, String currentVideoIndex, boolean instructor, String[] createdCourses, String[] enrolledCourses, String[] startedCourses, int students, Timestamp createdAt) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.currentVideoId = currentVideoId;
        this.currentCourseId = currentCourseId;
        this.currentVideoDuration = currentVideoDuration;
        this.currentVideoIndex = currentVideoIndex;
        this.instructor = instructor;
        this.createdCourses = createdCourses;
        this.enrolledCourses = enrolledCourses;
        this.startedCourses = startedCourses;
        this.students = students;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrentVideoId() {
        return currentVideoId;
    }

    public void setCurrentVideoId(String currentVideoId) {
        this.currentVideoId = currentVideoId;
    }

    public String getCurrentCourseId() {
        return currentCourseId;
    }

    public void setCurrentCourseId(String currentCourseId) {
        this.currentCourseId = currentCourseId;
    }

    public String getCurrentVideoDuration() {
        return currentVideoDuration;
    }

    public void setCurrentVideoDuration(String currentVideoDuration) {
        this.currentVideoDuration = currentVideoDuration;
    }

    public String getCurrentVideoIndex() {
        return currentVideoIndex;
    }

    public void setCurrentVideoIndex(String currentVideoIndex) {
        this.currentVideoIndex = currentVideoIndex;
    }

    public boolean isInstructor() {
        return instructor;
    }

    public void setInstructor(boolean instructor) {
        this.instructor = instructor;
    }

    public String[] getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(String[] createdCourses) {
        this.createdCourses = createdCourses;
    }

    public String[] getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(String[] enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public String[] getStartedCourses() {
        return startedCourses;
    }

    public void setStartedCourses(String[] startedCourses) {
        this.startedCourses = startedCourses;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
