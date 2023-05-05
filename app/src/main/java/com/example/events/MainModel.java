package com.example.events;


public class MainModel {

    String Club;
    String Date;
    String Location;

    String url;


    MainModel(){


    }

    public MainModel(String club, String date, String location, String url) {
        this.Club = club;
        this.Date = date;
        this.Location = location;
        this.url = url;

    }

    public String getClub() {
        return Club;
    }

    public void setClub(String club) {
        this.Club = club;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String club) {
        this.url = club;
    }


}
