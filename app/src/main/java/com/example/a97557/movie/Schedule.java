package com.example.a97557.movie;

public class Schedule {
    private int scheduleId;
    private int studioId;
    private int playId;
    private String date;
    private int scheduleTimeId;

    public Schedule(){

    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public int getPlayId() {
        return playId;
    }

    public void setPlayId(int playId) {
        this.playId = playId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScheduleTimeId() {
        return scheduleTimeId;
    }

    public void setScheduleTimeId(int scheduleTimeId) {
        this.scheduleTimeId = scheduleTimeId;
    }
}
