package com.example.user.task.model;

public class User {

    private String firstname;
    private String secondname;
    private String faculty;
    private String course;
    private String group;
    private String email;
    private Boolean isAdmin;
    private Boolean isBlocked = false;

    public User() {
    }

    public User(String firstname, String secondname, String faculty, String course, String group, String email, Boolean isAdmin) {
        this.firstname = firstname;
        this.secondname = secondname;
        this.faculty = faculty;
        this.course = course;
        this.group = group;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
