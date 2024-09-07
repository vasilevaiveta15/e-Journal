package com.example.ejournal.bean;

import java.util.List;

public class RegistrationStudent {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatedPassword;
    private String role;
    private String myClas;
    private String group;

    private List<RequestSubj> requestSubj;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMyClas() {
        return myClas;
    }

    public void setMyClas(String myClas) {
        this.myClas = myClas;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<RequestSubj> getRequestSubj() {
        return requestSubj;
    }

    public void setRequestSubj(List<RequestSubj> requestSubj) {
        this.requestSubj = requestSubj;
    }
}
