package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="Posts")
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    private int userId;
    private String title;
    private String body;

    public int getUserId() {
        return userId;
    }
    public void removeId () {
        userId = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
