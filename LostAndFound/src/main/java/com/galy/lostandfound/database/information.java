package com.galy.lostandfound.database;

public class information {
    public int _id;
    public String headline;
    public String content;
    public int lostorfound;
    public String number;

    public information() {
    }

    public information(String headline, String content, int lostorfound, String number) {
        this.headline = headline;
        this.content = content;
        this.lostorfound = lostorfound;
        this.number = number;
    }
}
