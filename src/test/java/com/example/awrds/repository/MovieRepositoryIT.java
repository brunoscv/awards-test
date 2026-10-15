package com.example.awrds.repository;

import com.example.awrds.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MovieRepositoryIT {

    @Autowired
    private MovieRepository repository;

    @Test
   void shouldReturnWinnersOrderedByYear() {
        List<Movie> movie = new ArrayList<>();

        movie.add(new Movie("Producer A","Movie 1",1990,true));
        movie.add(new Movie("Producer B","Movie 2",1990,true));
        movie.add(new Movie("Producer A","Movie 3",1995,true));


        repository.saveAll(movie);

        List<Movie> winners = repository.findByWinnerTrueOrderByYearAsc();

        assertThat(winners.get(0).getYear()).isEqualTo(1990);
        assertThat(winners.get(2).getYear()).isEqualTo(1995);
        assertThat(winners).allMatch(Movie::getWinner);
   }


}
