package com.example.alan.ntqmusicapp.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity (tableName = "song")
public class SongEntity implements Serializable{
    @PrimaryKey
    @NonNull
    private String id;

    private long id_long;

    private String song_name;

    private String singer;

    private String lyric;

    private String folder;

    private String image_url;

    public SongEntity() {
    }

    public SongEntity(@NonNull String id, long id_long, String song_name, String singer) {
        this.id = id;
        this.id_long = id_long;
        this.song_name = song_name;
        this.singer = singer;
    }

    public SongEntity(@NonNull String id, long id_long, String song_name, String singer, String folder) {
        this.id = id;
        this.id_long = id_long;
        this.song_name = song_name;
        this.singer = singer;
        this.folder = folder;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    public long getId_long() {
        return id_long;
    }

    public void setId_long(long id_long) {
        this.id_long = id_long;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
