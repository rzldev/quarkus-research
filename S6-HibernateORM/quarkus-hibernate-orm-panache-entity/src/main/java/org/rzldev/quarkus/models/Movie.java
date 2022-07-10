package org.rzldev.quarkus.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="movies")
public class Movie extends PanacheEntity {

    @Column(length=100)
    public String title;

    @Column
    public String description;

    @Column(length=50)
    public String director;

    @Column(length=50)
    public String country;

}
