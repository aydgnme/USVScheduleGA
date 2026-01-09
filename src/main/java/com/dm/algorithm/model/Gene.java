package com.dm.algorithm.model;

public class Gene {
    private String courseId;
    private String teacherId;
    private String roomId;
    private String studentGroupId;
    private int timeslot; // Represents a unique block (e.g., 0-39 for a 40-hour week)

    public Gene() {}

    public Gene(String courseId, String teacherId, String roomId, String studentGroupId, int timeslot) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.roomId = roomId;
        this.studentGroupId = studentGroupId;
        this.timeslot = timeslot;
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getStudentGroupId() { return studentGroupId; }
    public void setStudentGroupId(String studentGroupId) { this.studentGroupId = studentGroupId; }

    public int getTimeslot() { return timeslot; }
    public void setTimeslot(int timeslot) { this.timeslot = timeslot; }
}