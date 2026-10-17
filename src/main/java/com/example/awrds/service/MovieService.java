package com.example.awrds.service;

import com.example.awrds.dto.response.IntervalResponse;
import com.example.awrds.dto.response.MovieResponse;
import com.example.awrds.dto.response.ResultResponse;
import com.example.awrds.mapper.MovieMapper;
import com.example.awrds.model.Movie;
import com.example.awrds.repository.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final MovieMapper mapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.repository = movieRepository;
        this.mapper = movieMapper;
    }

   //Tratar Dto como Dto
    //mlehorar nomes dos metodos
    //proteced serve pra ficar visivel dentro do mesmo "andar" ou seja
    //ta dentro da mesma pasta nsse caso service.
    //pra fora eh private
    public List<MovieResponse> getAll() {
        List<Movie> winners = repository.findByWinnerTrueOrderByYearAsc();
        return mapper.toResponseList(winners);
    }

    //olhar os privates
    public List<Movie> prepareMovies(List<MovieResponse> movies) {
        List<Movie> entities = new ArrayList<>();

        for(MovieResponse movie : movies) {
            List<String> producers = this.parseProducers(movie.getProducer());

            for(String producer : producers) {
                producer = producer.trim();

                Movie entity = new Movie(
                        producer,
                        movie.getTitle(),
                        movie.getYear(),
                        movie.getWinner()
                );

                entities.add(entity);
            }
        }

        return  entities;
    }

    private List<String> parseProducers(String producers) {
        if (producers == null || producers.isBlank()) {
            //Collections.emptyList() eh mais interessante de new ArrayList<>();
            return Collections.emptyList();
        }

        String cleanedField = producers.trim().replaceAll("^\"|\"$", "");
        String normalized = cleanedField.replaceAll("(?i)\\s+and\\s+", ",");
        String[] parts = normalized.split(",");

        List<String> producersList = new ArrayList<>();
        for (String part : parts) {
            String producer = part.trim()
                    .replaceAll("\\s+", " ")
                    .replaceAll("^\"|\"$", "");
            if (!producer.isEmpty()) {
                producersList.add(producer);
            }
        }

        return producersList;
    }

    public Map<String, List<Integer>> producerYearsMap(List<MovieResponse> movies) {

        //Hash Set aqui nos qyeas para nao permitir os caras repetidos.
        //linked hash set garante a ordem dos elemetnos
        Map<String, List<Integer>> winsByProducer = new HashMap<>();

        for(MovieResponse movie: movies) {

            if(!movie.getWinner()) {
                continue;
            }

            String producer = movie.getProducer();

            if(!winsByProducer.containsKey(producer)) {
                winsByProducer.put(producer, new ArrayList<>());
            }

            List<Integer> years = winsByProducer.get(producer);

            if(!years.contains(movie.getYear())) {
                years.add(movie.getYear());
            }

        }

        return winsByProducer;

    }

    public ResultResponse processIntervals(Map<String, List<Integer>> producerYearsMap) {
        //olhar isEmpty ou isBlank sao redundantes
        if (producerYearsMap == null || producerYearsMap.isEmpty()) {
            //estudar sobre um construtor vazio aqui.
            return new ResultResponse(Collections.emptyList(), Collections.emptyList());
        }

        //Thread-safe
        Queue<IntervalResponse> all = new ConcurrentLinkedQueue<>();

        //entry, entrySet
        producerYearsMap.entrySet().parallelStream().forEach(entry -> {
            String producer = entry.getKey();
            //Olhar o LinkedList que isso faz com que não precise o collections sort
            //Set Hashset nao permite addos iguais.
            List<Integer> years = entry.getValue();
            Collections.sort(years);

            //streams()
            //tentar fazer com streams years.stream.reduce()
            //estudar
            //ja quero um reduce que traz o min e o max
            if (years.size() >= 2) {
                for (int i = 0; i < years.size() - 1; i++) {
                    int prev = years.get(i);
                    int next = years.get(i + 1);
                    int interval = next - prev;

                    all.add(new IntervalResponse(producer, interval, prev, next));
                }
            }
        });

        if (all.isEmpty()) {
            return new ResultResponse(Collections.emptyList(), Collections.emptyList());
        }

        int minInterval = all.stream().mapToInt(IntervalResponse::getInterval).min().orElse(0);
        int maxInterval = all.stream().mapToInt(IntervalResponse::getInterval).max().orElse(0);

        List<IntervalResponse> minList = all.stream()
                .filter(i -> i.getInterval() == minInterval).toList();

        List<IntervalResponse> maxList = all.stream()
                .filter(i -> i.getInterval() == maxInterval)
                .collect(Collectors.toList());

        return new ResultResponse(minList, maxList);
    }

}
