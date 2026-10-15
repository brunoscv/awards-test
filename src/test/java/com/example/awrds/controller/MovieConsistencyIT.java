package com.example.awrds.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MovieConsistencyIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnIntervalsFieldsAndValues() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies/intervals"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.min").exists())
                .andExpect(jsonPath("$.min[0].producer").exists())
                .andExpect(jsonPath("$.min[0].interval").exists())
                .andExpect(jsonPath("$.min[0].previousWin").exists())
                .andExpect(jsonPath("$.min[0].followingWin").exists())
            .andExpect(jsonPath("$.max").exists())
                .andExpect(jsonPath("$.max[0].producer").exists())
                .andExpect(jsonPath("$.max[0].interval").exists())
                .andExpect(jsonPath("$.max[0].previousWin").exists())
                .andExpect(jsonPath("$.max[0].followingWin").exists())
        ;
    }
}
