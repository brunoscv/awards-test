package com.example.awrds.dto.response;


//public record IntervaloDetalhe(String producer, int interval, int previousWin, int followingWin) {}
public class IntervalResponse {

    private final String producer;
    private final Integer interval;
    private final Integer previousWin;
    private final Integer followingWin;

    public IntervalResponse(String producer, Integer interval, Integer previousWin, Integer followingWin) {
        this.producer = producer;
        this.interval = interval;
        this.previousWin = previousWin;
        this.followingWin = followingWin;
    }

    public String getProducer() {
        return producer;
    }

    public Integer getInterval() {
        return interval;
    }

    public Integer getPreviousWin() {
        return previousWin;
    }

    public Integer getFollowingWin() {
        return followingWin;
    }
}
