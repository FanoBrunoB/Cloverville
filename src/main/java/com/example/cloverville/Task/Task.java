package com.example.cloverville.Task;

public class Task {

    private int id;
    private String title;
    private String status;
    private String deadline;
    private int greenActionAssigned;
    private int residentAssigned;

    public Task(int id, String title, String status, String deadline, int greenActionAssigned, int residentAssigned) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.greenActionAssigned = greenActionAssigned;
        if(residentAssigned != -1){
            this.residentAssigned = residentAssigned;
            this.status = status;
        } else {
            this.residentAssigned = -1;
            this.status = "Open";
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getGreenActionAssigned() {
        return greenActionAssigned;
    }

    public void setGreenActionAssigned(int greenActionAssigned) {
        this.greenActionAssigned = greenActionAssigned;
    }

    public int getResidentAssigned() {
        return residentAssigned;
    }

    public void setResidentAssigned(int residentAssigned) {
        this.residentAssigned = residentAssigned;
    }
}
