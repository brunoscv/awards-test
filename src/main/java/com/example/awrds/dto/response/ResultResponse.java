package com.example.awrds.dto.response;


import java.util.List;

public class ResultResponse {
    List<IntervalResponse> min;
    List<IntervalResponse> max;

    public ResultResponse(List<IntervalResponse> min, List<IntervalResponse> max) {
        this.min = min;
        this.max = max;
    }


    public List<IntervalResponse> getMax() {
        return max;
    }

    public List<IntervalResponse> getMin() {
        return min;
    }
}
