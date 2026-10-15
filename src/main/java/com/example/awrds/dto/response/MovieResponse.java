package com.example.awrds.dto.response;

public class MovieResponse {

    private Long id;
    private final String producer;
    private final String title;
    private final Integer year;
    private final boolean winner;

    public MovieResponse(String producer, String title, Integer year, boolean winner) {
        this.producer = producer;
        this.title = title;
        this.year = year;
        this.winner = winner;
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

    public Long getId() {
        return id;
    }

    public boolean getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "id=" + id +
                ", producer='" + producer + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", winner=" + winner +
                '}';
    }
}
