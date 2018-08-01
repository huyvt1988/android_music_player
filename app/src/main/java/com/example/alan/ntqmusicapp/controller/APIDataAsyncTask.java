package com.example.alan.ntqmusicapp.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.activity.ActivityListSong;
import com.example.alan.ntqmusicapp.model.Commons;
import com.example.alan.ntqmusicapp.room.AppDatabase;
import com.example.alan.ntqmusicapp.room.DataGenerator;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class APIDataAsyncTask extends AsyncTask<String, Integer, List<SongEntity>> {
    private Context context;
    private AppDatabase database;

    public APIDataAsyncTask(Context ct, AppDatabase database) {
        this.context = ct;
        this.database = database;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<SongEntity> doInBackground(String... strings) {
        String json_data = new Commons().getJSONStringFromURL(strings[0]);
        List<SongEntity> songList = null;
        if (json_data != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                songList = mapper.readValue(json_data, mapper.getTypeFactory().constructCollectionType(List.class, SongEntity.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataGenerator.with(database).insertSongs(songList);
        }
        return songList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<SongEntity> songList) {
        super.onPostExecute(songList);
        if (songList != null)
            Toast.makeText(context, "API Data loaded!", Toast.LENGTH_SHORT).show();
    }
}
