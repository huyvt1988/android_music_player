package com.example.alan.ntqmusicapp.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.alan.ntqmusicapp.activity.act_list_song;
import com.example.alan.ntqmusicapp.model.Commons;
import com.example.alan.ntqmusicapp.room.AppDatabase;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class APIDataAsyncTask extends AsyncTask<String, Integer, Void>{
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
    protected Void doInBackground(String... strings) {
        String json_data = new Commons().getJSONStringFromURL(strings[0]);
        ObjectMapper mapper = new ObjectMapper();
        List<SongEntity> songList = null;
        try {
            songList = mapper.readValue(json_data, mapper.getTypeFactory().constructCollectionType(List.class,SongEntity.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        database.songDao().insertMultipleSongs(songList);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Data loaded!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, act_list_song.class);
        context.startActivity(intent);
    }
}
