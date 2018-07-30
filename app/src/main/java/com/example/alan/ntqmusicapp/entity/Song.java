package com.example.alan.ntqmusicapp.entity;

public class Song {
    private String id;
    private String song_name;
    private String singer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Song() {
    }

    public Song(String id, String song_name, String singer) {
        this.id = id;
        this.song_name = song_name;
        this.singer = singer;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
