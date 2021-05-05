package com.example.trainqr.models;

import java.util.List;

public class UserModel {
    private String name;
    private String email;
    private String gender;
    private int age;
    private String address;
    private List<String> previous;
    private List<String> current;

    public List<String> getPrevious() {
        return previous;
    }

    public void setPrevious(List<String> previous) {
        this.previous = previous;
    }

    public List<String> getCurrent() {
        return current;
    }

    public void setCurrent(List<String> current) {
        this.current = current;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserModel() {
    }
}
