package com.example.trainqr.models;

import java.util.List;

public class TrainModel {
    private List<String> Fare;
    private String from;
    private List<String> path;
    private List<String> time;
    private String timings;
    private String to;
    private String train_name;
    private String train_id;

    public TrainModel() {
    }

    public List<String> getFare() {
        return Fare;
    }

    public void setFare(List<String> Fare) {
        this.Fare = Fare;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getTo() {
        return to;
    }
    public String getTrain_id() {
            return train_id;
    }
    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String toString(){
        return String.format("%s\n%s\n%s\n%s\n%s\n",train_name,train_id,to,Fare.toString(),timings);
    }

}
