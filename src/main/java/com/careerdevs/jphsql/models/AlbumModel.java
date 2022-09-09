package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="Albums")
public class AlbumModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int id;
    private int userId;
    private String title;

    public void removeId () {
        id = 0;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
