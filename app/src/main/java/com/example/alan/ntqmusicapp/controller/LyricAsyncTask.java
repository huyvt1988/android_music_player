package com.example.alan.ntqmusicapp.controller;

import android.os.AsyncTask;

import com.example.alan.ntqmusicapp.entity.Lyric;
import com.example.alan.ntqmusicapp.entity.SongLyric;
import com.example.alan.ntqmusicapp.model.Commons;
import com.google.gson.Gson;

public class LyricAsyncTask extends AsyncTask<String, Void, String> {
    SongLyric songLyric;

    public LyricAsyncTask(SongLyric songLyric) {
        this.songLyric = songLyric;
    }

    @Override
    protected String doInBackground(String... strings) {
        String json_data = new Commons().getJSONStringFromURL(strings[0]);
        String result = "";
        try {
            Lyric lyric = new Gson().fromJson(json_data, Lyric.class);
            result = lyric.getIntro() + "\n" + lyric.getBridge() + "\n" + lyric.getChorus() + "\n" + lyric.getEnding();
            result = result.replaceAll("\\n", "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result.isEmpty()) {
            songLyric.setLyric(result);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
