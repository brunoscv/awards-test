package com.example.awrds.config;

import com.example.awrds.dto.response.MovieResponse;
import com.example.awrds.model.Movie;
import com.example.awrds.repository.MovieRepository;
import com.example.awrds.service.DataService;
import com.example.awrds.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MovieRepository repository;
    private final MovieService service;
    private final DataService dataService;

    public DataInitializer(MovieRepository movieRepository, MovieService movieService, DataService dataService) {
        this.repository = movieRepository;
        this.service = movieService;
        this.dataService = dataService;
    }

    public void run(String ...args) throws IOException {
        List<MovieResponse> movies = dataService.readCsv("01_with_errors.csv");

        List<Movie> entities = service.prepareMovies(movies);

        //quando trabalha com essa arquitetura isolamento de camadas.
        //transferir toda a responsabilidade para o service
        //o intilizser nao deveria saber o que é repository
        //entao tirar tudo daqui isso adaqui
        //tirar o repository daqui;

        //List<Movie> = entites = service.saveMovies();

        repository.saveAll(entities);
    }
}
