package com.example.alan.ntqmusicapp.activity;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.controller.APILyricAsyncTask;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.example.alan.ntqmusicapp.service.MusicService;

import com.example.alan.ntqmusicapp.service.MusicService.MusicBinder;

import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;

public class act_player_base extends AppCompatActivity {
    TextView txt_song_title, txt_lyric, txt_song_name_player, txt_singer_player, txt_time_song, txt_time_total;
    ImageView img_setting_player;
    ImageButton btn_prev, btn_play, btn_next;
    SeekBar skb_player;

    SongEntity songEntity;

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    public static final String API_LYRIC = "https://raw.githubusercontent.com/MrNinja/android_music_app_api/master/api/lyric/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lt_player);
        initControl();
        initData();
        initEvent();
        startService();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        songEntity = (SongEntity) bundle.getSerializable("songEntity");
        txt_song_title.setText(songEntity.getSong_name());
        txt_song_name_player.setText(songEntity.getSong_name());
        txt_singer_player.setText(songEntity.getSinger());
        APILyricAsyncTask asyncTask = new APILyricAsyncTask(this);
        asyncTask.execute(API_LYRIC + songEntity.getId());
    }

    private void initControl() {
        txt_song_title = (TextView) findViewById(R.id.txt_song_title);
        txt_lyric = (TextView) findViewById(R.id.txt_lyric);
        txt_song_name_player = (TextView) findViewById(R.id.txt_song_name_player);
        txt_singer_player = (TextView) findViewById(R.id.txt_singer_player);
//        txt_time_song = (TextView) findViewById(R.id.txt_time_song);
//        txt_time_total = (TextView) findViewById(R.id.txt_time_total);
//        img_setting_player = (ImageView) findViewById(R.id.img_setting_player);
        btn_prev = (ImageButton) findViewById(R.id.btn_prev);
        btn_play = (ImageButton) findViewById(R.id.btn_play);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
//        skb_player = (SeekBar) findViewById(R.id.skb_player);

        act_list_song.controller.show();
    }

    private void initEvent() {

    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
//            musicSrv.setList(songList);
//            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void startService() {
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
}
