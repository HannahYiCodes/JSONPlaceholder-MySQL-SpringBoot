package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="Photos")
public class PhotoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int albumId;
    private String title;
    private String url;
    private String thumbnailUrl;

    public int getAlbumId() {
        return albumId;
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

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
