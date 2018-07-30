package com.example.alan.ntqmusicapp.controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.entity.Lyric;
import com.example.alan.ntqmusicapp.model.Commons;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class APILyricAsyncTask extends AsyncTask<String, Void, String> {
    Activity activity;

    public APILyricAsyncTask(Activity activity) {
        this.activity = activity;
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
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        TextView textView = activity.findViewById(R.id.txt_lyric);
        textView.setText(s);
    }
}
