package com.example.alan.ntqmusicapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.controller.APIDataAsyncTask;
import com.example.alan.ntqmusicapp.room.AppDatabase;
import com.example.alan.ntqmusicapp.room.SongEntity;

import java.util.ArrayList;
import java.util.List;

public class act_flash extends Activity {
    private List<SongEntity> songList;
    AppDatabase database;
    public static final String API_LIST_SONG = "https://raw.githubusercontent.com/MrNinja/android_music_app_api/master/api/list_music";

    public static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lt_flash);

        songList = new ArrayList<>();
        //check permission
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            createDatabase();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    createDatabase();
                }
                break;

            default:
                break;
        }
    }

    public void createDatabase(){
        //create database
        database = AppDatabase.getAppDatabase(this);
        if (database.songDao().getAllSong().size() == 0) {
//            APIDataAsyncTask asyncTask = new APIDataAsyncTask(this,database);
//            asyncTask.execute(API_LIST_SONG);

            //external store
            externalSongAsyncTask externalSongAsyncTask = new externalSongAsyncTask();
            externalSongAsyncTask.execute();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(act_flash.this, act_list_song.class);
                    startActivity(intent);
                    finish();
                }
            }, 2 * 1000);
        }
    }

    //method to retrieve song info from device
    public void getSongList() {
        //query external audio
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisIdLong = musicCursor.getLong(idColumn);
                String thisId = String.valueOf(musicCursor.getLong(idColumn));
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new SongEntity(thisId, thisIdLong, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    public class externalSongAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getSongList();
            database.songDao().insertMultipleSongs(songList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(act_flash.this, "Data loaded!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(act_flash.this, act_list_song.class);
            startActivity(intent);
        }
    }
}
