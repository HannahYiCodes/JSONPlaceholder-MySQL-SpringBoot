package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="User")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    @Column(length = 50)
    private String website;

    public void removeId() {
        id = 0;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }
}