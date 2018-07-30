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

    public void generateSong(List<SongEntity> songList) {
        if (dataBase == null)
            return;
        dataBase.songDao().insertMultipleSongs(songList);
    }
}
