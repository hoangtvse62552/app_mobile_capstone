package com.example.tfs.dto;

public class Users {


    private int id;
    private String username;
    private String fullname;
    private String mail;
    private String phoneNumber;

    public Users(int id, String username, String fullname, String mail, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
    }

    public Users() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
