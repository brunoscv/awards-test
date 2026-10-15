package com.example.awrds.controller;

import com.example.awrds.dto.response.MovieResponse;
import com.example.awrds.dto.response.ResultResponse;
import com.example.awrds.mapper.MovieMapper;
import com.example.awrds.model.Movie;
import com.example.awrds.repository.MovieRepository;
import com.example.awrds.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService movieService) {
        this.service = movieService;
    }

    @GetMapping
    public List<MovieResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/intervals")
    public ResultResponse getMovies() {

        List<MovieResponse> movies = service.getAll();
        Map<String, List<Integer>> produceredYearsMap = service.producerYearsMap(movies);

        return service.processIntervals(produceredYearsMap);

    }

}
