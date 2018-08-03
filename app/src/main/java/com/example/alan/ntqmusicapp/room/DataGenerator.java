package com.example.alan.ntqmusicapp.room;

import java.util.List;

public class DataGenerator {

    private static DataGenerator instance;
    private static AppDatabase dataBase;

    public static DataGenerator with(AppDatabase appDataBase) {
        if (dataBase == null)
            dataBase = appDataBase;

        if (instance == null)
            instance = new DataGenerator();

        return instance;
    }

    public void insertSong(SongEntity song) {
        if (dataBase == null)
            return;
        dataBase.songDao().insertSong(song);
    }

    public void insertSongs(List<SongEntity> songList) {
        if (dataBase == null)
            return;
        dataBase.songDao().insertMultipleSongs(songList);
    }

    public List<SongEntity> getAllSong() {
        if (dataBase == null)
            return null;
        return dataBase.songDao().getAllSong();
    }

    public List<SongEntity> getSongsByName() {
        if (dataBase == null)
            return null;
        return dataBase.songDao().getSongsByName();
    }

    public List<SongEntity> getSongsByFolder() {
        if (dataBase == null)
            return null;
        return dataBase.songDao().getSongsByFolder();
    }

    public List<SongEntity> getSongsByAPI() {
        if (dataBase == null)
            return null;
        return dataBase.songDao().getSongsByAPI();
    }

    public List<SongEntity> getSongsExternal() {
        if (dataBase == null)
            return null;
        return dataBase.songDao().getSongsExternal();
    }
}
