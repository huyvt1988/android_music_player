package com.example.alan.ntqmusicapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SongDao {

    @Insert
    void insertSong(SongEntity song);

    @Insert
    void insertMultipleSongs(List<SongEntity> lisSongs);

    @Query("Select * From song")
    List<SongEntity> getAllSong();

    @Query("Select * From song Order By song_name ASC")
    List<SongEntity> getAllSongByName();
}
