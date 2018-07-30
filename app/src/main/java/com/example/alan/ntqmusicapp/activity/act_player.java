package com.example.alan.ntqmusicapp.activity;

import android.content.Intent;
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


public class act_player extends AppCompatActivity {
    TextView txt_song_title, txt_lyric, txt_song_name_player, txt_singer_player, txt_time_song, txt_time_total;
    ImageView img_setting_player;
    ImageButton btn_prev, btn_play, btn_next;
    SeekBar skb_player;

    SongEntity songEntity;

    private boolean paused;

    private MusicService musicSrv = act_list_song.musicSrv;

    int posSong;

    public static final String API_LYRIC = "https://raw.githubusercontent.com/MrNinja/android_music_app_api/master/api/lyric/";

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
        if (!act_list_song.playbackPaused) {
            btn_play.setImageResource(R.mipmap.av_pause);
        } else {
            btn_play.setImageResource(R.mipmap.av_play);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;

//        musicSrv.pausePlayer();
//        if(!act_list_song.playbackPaused){
//            act_list_song.playbackPaused = true;
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        this.unbindService(act_list_song.musicConnection);
//        Intent intent = new Intent(act_player.this, MusicService.class);
//        this.stopService(intent);
//        musicSrv.stopPlayer();

//        musicSrv.pausePlayer();
//        if(!act_list_song.playbackPaused){
//            act_list_song.playbackPaused = true;
//        }

    }

    //song select
    public void songPicked(int pos) {
        musicSrv.setSong(pos);
        musicSrv.playSong();
        if (act_list_song.playbackPaused) {
            act_list_song.playbackPaused = false;
        }
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
        //play
//        songPicked(posSong);

        //play icon change
//        if (act_list_song.playbackPaused == false) {
//            btn_play.setImageResource(R.mipmap.av_pause);
//        }

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
                Intent intent = new Intent(act_player.this, act_setting.class);
                startActivity(intent);
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (act_list_song.playbackPaused) {
                    musicSrv.start();
                    act_list_song.playbackPaused = !act_list_song.playbackPaused;
                    btn_play.setImageResource(R.mipmap.av_pause);
                } else {
                    musicSrv.pausePlayer();
                    act_list_song.playbackPaused = !act_list_song.playbackPaused;
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
                if (act_list_song.playbackPaused) {
                    act_list_song.playbackPaused = false;
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
                if (act_list_song.playbackPaused) {
                    act_list_song.playbackPaused = false;
                    btn_play.setImageResource(R.mipmap.av_pause);
                }
            }
        });
    }

    public boolean isPlaying() {
        if (musicSrv != null)
            return musicSrv.isPng();
        return false;
    }

    public void setInfoSong(SongEntity songEntity) {
        txt_song_title.setText(songEntity.getSong_name());
        txt_song_name_player.setText(songEntity.getSong_name());
        txt_singer_player.setText(songEntity.getSinger());
    }

}
