package com.example.a97557.movie;

/**
 * Created by a on 2017/5/31.
 */

public class StudioListBean {
    private int id;
    private String studioName;
    private String studioMovie;
    private int studioSeat;
    private String studioNumber;
    private int studioRow;
    private int studioColumn;
    private String studioPosition;
    private int image;

    public StudioListBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudioMovie() {
        return studioMovie;
    }

    public void setStudioMovie(String studioMovie) {
        this.studioMovie = studioMovie;
    }

    public int getStudioColumn() {
        return studioColumn;
    }

    public void setStudioColumn(int studioColumn) {
        this.studioColumn = studioColumn;
    }

    public int getStudioRow() {
        return studioRow;
    }

    public void setStudioRow(int studioRow) {
        this.studioRow = studioRow;
    }
    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public String getStudioPosition() {
        return studioPosition;
    }

    public void setStudioPosition(String employeePosition) {
        this.studioPosition = studioPosition;
    }

    public int getStudioSeat() {
        return studioSeat;
    }

    public void setStudioSeat(int studioSeat) {
        this.studioSeat = studioSeat;
    }

    public String getStudioNumber() {
        return studioNumber;
    }

    public void setStudioNumber(String studioNumber) {
        this.studioNumber = studioNumber;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}