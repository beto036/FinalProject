package com.example.admin.finalproject.entities;

/**
 * Created by admin on 11/6/2016.
 */

public class User {

    private String name;
    private String lastname;
    private Integer age;
    private String orientation;
    private String username;
    private String password;
    private String email;

    public User() {
    }

    public User(String name, String lastname, Integer age,
                   String orientation, String username, String password, String email) {
        super();
        this.name = name;
        this.lastname = lastname;
        this.age = age;
        this.orientation = orientation;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public String getOrientation() {
        return orientation;
    }
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", orientation='" + orientation + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
