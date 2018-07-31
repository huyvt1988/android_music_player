package com.example.alan.ntqmusicapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.controller.APILyricAsyncTask;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.example.alan.ntqmusicapp.service.MusicService;


public class ActivityPlayer extends AppCompatActivity {
    private TextView txt_song_title, txt_lyric, txt_song_name_player, txt_singer_player, txt_time_song, txt_time_total;
    private ImageView img_setting_player;
    private ImageButton btn_prev, btn_play, btn_next;
    private SeekBar skb_player;

    private SongEntity songEntity;

    private boolean paused;

    private MusicService musicSrv = ActivityListSong.musicSrv;

    private int posSong;

    public static final String API_LYRIC = "https://raw.githubusercontent.com/MrNinja/android_music_app_api/master/api/lyric/";

    private boolean isBackGround = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lt_player);
        initControl();
        initData();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            paused = false;
        }
        //update status mini player
        if (musicSrv != null) {
            setInfoSong(musicSrv.getInfo());
        }
        if (ActivityListSong.playbackPaused) {
            btn_play.setImageResource(R.mipmap.av_play);
        } else {
            btn_play.setImageResource(R.mipmap.av_pause);
        }

        //run on background
        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        isBackGround = sharedPreferences.getBoolean("isBackGround", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;

        if(!isBackGround) {
            musicSrv.pausePlayer();
            if(!ActivityListSong.playbackPaused){
                ActivityListSong.playbackPaused = true;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {

        //get data
        if (getIntent().getBundleExtra("bundle") != null) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            songEntity = (SongEntity) bundle.getSerializable("songEntity");
            setInfoSong(songEntity);
            APILyricAsyncTask asyncTask = new APILyricAsyncTask(this);
            asyncTask.execute(API_LYRIC + songEntity.getId());

            posSong = getIntent().getBundleExtra("bundle").getInt("position");
        }
    }

    private void initControl() {
        txt_song_title = (TextView) findViewById(R.id.txt_song_title);
        txt_lyric = (TextView) findViewById(R.id.txt_lyric);
        txt_song_name_player = (TextView) findViewById(R.id.txt_song_name_player);
        txt_singer_player = (TextView) findViewById(R.id.txt_singer_player);
//        txt_time_song = (TextView) findViewById(R.id.txt_time_song);
//        txt_time_total = (TextView) findViewById(R.id.txt_time_total);
        img_setting_player = (ImageView) findViewById(R.id.img_setting_player);
        btn_prev = (ImageButton) findViewById(R.id.btn_prev);
        btn_play = (ImageButton) findViewById(R.id.btn_play);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
//        skb_player = (SeekBar) findViewById(R.id.skb_player);
    }

    private void initEvent() {
        img_setting_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPlayer.this, ActivitySetting.class);
                startActivity(intent);
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityListSong.playbackPaused) {
                    musicSrv.start();
                    ActivityListSong.playbackPaused = !ActivityListSong.playbackPaused;
                    btn_play.setImageResource(R.mipmap.av_pause);
                } else {
                    musicSrv.pausePlayer();
                    ActivityListSong.playbackPaused = !ActivityListSong.playbackPaused;
                    btn_play.setImageResource(R.mipmap.av_play);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext();
                songEntity = musicSrv.getInfo();
                setInfoSong(songEntity);
                if (ActivityListSong.playbackPaused) {
                    ActivityListSong.playbackPaused = false;
                    btn_play.setImageResource(R.mipmap.av_pause);
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();
                songEntity = musicSrv.getInfo();
                setInfoSong(songEntity);
                if (ActivityListSong.playbackPaused) {
                    ActivityListSong.playbackPaused = false;
                    btn_play.setImageResource(R.mipmap.av_pause);
                }
            }
        });
    }

    public void setInfoSong(SongEntity songEntity) {
        txt_song_title.setText(songEntity.getSong_name());
        txt_song_name_player.setText(songEntity.getSong_name());
        txt_singer_player.setText(songEntity.getSinger());
    }

}
