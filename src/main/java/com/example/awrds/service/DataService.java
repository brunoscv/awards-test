package com.example.awrds.service;

import com.example.awrds.dto.response.MovieResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class DataService {

    private static final Set<String> REQUIRED_COLUMNS = Set.of("producers", "title", "year", "winner");
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);
    public DataService() {}

    public List<MovieResponse> readCsv(String filename) throws IOException {

        List<MovieResponse> movies = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("data/" + filename);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String headLine = reader.readLine();
            if (headLine == null) {
                throw new IllegalStateException("O CSV pode estar vazio.");
            }

            Map<String, Integer> columns = writeColumns(headLine);
            validateHeaders(columns);

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                String[] values = line.split(";", -1);

                if (values.length <= Collections.max(columns.values())) {
                    logger.warn("A linha possui menos elementos que o necessário");
                    continue;
                }

                String producers = values[columns.get("producers")];
                String title = values[columns.get("title")];
                String year = values[columns.get("year")];
                boolean winner = values[columns.get("winner")].equalsIgnoreCase("yes");

                if( producers == null || producers.isBlank() ||
                title == null || title.isBlank()) {
                    logger.warn("O filme foi ignorado por possivelmente conter dados em branco, nulo ou mal formatados.");
                    continue;
                }

                int castYear;
                try {
                    if (year == null || year.isBlank()) {
                        logger.warn("O filme foi ignorado por conter o ano com valor nulo, em branco");
                        continue;
                    }
                    castYear = Integer.parseInt(year);
                } catch (NumberFormatException e) {
                    logger.error("Erro ao converter o ano disponibilizado em ano inteiro.");
                    continue;
                }

                movies.add(new MovieResponse(
                        producers,
                        title,
                        castYear,
                        winner
                ));
            }
            return movies;
        }
    }
    public void validateHeaders(Map<String, Integer> columns) {
        Set<String> missing = new HashSet<>(REQUIRED_COLUMNS);
        missing.removeAll(columns.keySet());

        if(!missing.isEmpty()) {
            throw new IllegalArgumentException("CSV não tem os campos obrigatórios: " + missing);
        }
    }
    public Map<String, Integer> writeColumns(String headerLine) {
        String[] headers = headerLine.split(";");
        Map<String, Integer> columns = new HashMap<>();
        for(int i = 0; i < headers.length; i++) {
            columns.put(headers[i].trim().toLowerCase(), i);
        }

        return columns;
    }
}
