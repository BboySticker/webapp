package com.csye6225.webservice.RESTfulWebService.Entity.Recipe;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "id")
    private String id;  // uuid

    @Column(name = "url")
    private String url;

    @OneToOne(mappedBy = "image")
    private Recipe recipe;

    public Image() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
