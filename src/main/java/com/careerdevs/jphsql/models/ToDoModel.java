package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="ToDos")
public class ToDoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private String title;
    private boolean completed;

    public int getUserId() {
        return userId;
    }

    public void removeId () {
        id = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}
