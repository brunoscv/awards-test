package com.example.awrds.model;

import jakarta.persistence.*;

@Entity
@Table(name = "awards")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String producer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "year_movie")
    private Integer year;

    @Column(nullable = false)
    private boolean winner = false;

    public Movie() {}

    public Movie(String producer, String title, Integer year, boolean winner) {
        this.producer = producer;
        this.title = title;
        this.year = year;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public String getProducer() {
        return producer;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public boolean getWinner() {
        return winner;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
