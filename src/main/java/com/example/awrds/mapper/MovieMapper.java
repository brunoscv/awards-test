package com.example.awrds.mapper;

import com.example.awrds.dto.response.MovieResponse;
import com.example.awrds.model.Movie;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {


    //modelMapper -> faz  mesma coisa, lib do java
    //mapStruct -> olhar tbm
    public MovieResponse toResponse(Movie movie) {
        if(movie == null) {
            return null;
        }

        return new MovieResponse(
                movie.getProducer(),
                movie.getTitle(),
                movie.getYear(),
                movie.getWinner()
        );
    }

    public Movie toEntity(MovieResponse response) {
        if(response == null) {
            return null;
        }

        Movie movie = new Movie();
        movie.setProducer(response.getProducer());
        movie.setTitle(response.getTitle());
        movie.setYear(response.getYear());
        movie.setWinner(response.getWinner());

        return movie;
    }

    public List<MovieResponse> toResponseList(List<Movie> movies) {
        return movies.stream().map(this::toResponse).toList();
    }

    public List<Movie> toMovieList(List<MovieResponse> responses) {
        return responses.stream().map(this::toEntity).toList();
    }
}
