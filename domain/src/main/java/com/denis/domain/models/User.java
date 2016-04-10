package com.denis.domain.models;

/**
 * Created by denis on 4/10/16.
 */
public class User {
    private String id;
    private String name;
    private String email;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
}
