package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="Comment")
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int postId;
    private String name;
    private String email;
    @Column(length = 300) // columnDefinition = "VARCHAR(1000)"
    // create method - length of body - determine the highest

    private String body;

    public int getPostId() {
        return postId;
    }

    public void removeId () {
        id = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }
}
