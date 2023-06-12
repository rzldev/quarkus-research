package org.rzldev.quarkus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="movies")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length=100)
    private String title;

    private String description;

    private String director;

    private String country;

}
