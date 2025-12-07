package com.example.cloverville.GreenAction;

public class GreenAction {

    private int id;
    private String title;
    private String description;
    private int points;

    public GreenAction(int id, String title, String description, int points) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
