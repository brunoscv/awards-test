package com.example.awrds.repository;

import com.example.awrds.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByWinnerTrueOrderByYearAsc();
}
