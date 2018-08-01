package com.example.alan.ntqmusicapp.entity;

public class SongLyric {
    private String id;
    private String lyric;

    public SongLyric() {
    }

    public SongLyric(String id, String lyric) {
        this.id = id;
        this.lyric = lyric;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
