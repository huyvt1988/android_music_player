package com.example.alan.ntqmusicapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.example.alan.ntqmusicapp.service.MusicService;
import com.mikhaellopez.circularimageview.CircularImageView;


public class ActivityPlayer extends MyActivity {
    private TextView txt_lyric, txt_song_name_player, txt_singer_player, txt_time_song, txt_time_total;
    private ImageView img_setting_player;
    private ImageButton btn_prev, btn_play, btn_next;
    private SeekBar skb_player;
    private static CircularImageView img_disk;

    private SongEntity songEntity;

    private boolean paused;

    private MusicService musicSrv = ActivityListSong.getMusicSrv();

    private int posSong;

    private Handler handler = null;

    private Animation animation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "onCreate-player", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.lt_player);
        initControl();
        initData();
        initEvent();

        //seekbar, song info
        updateSongInterface();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(this, "onStart-player", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "onResume-player", Toast.LENGTH_SHORT).show();
        if (paused) {
            paused = false;
        }

        if (ActivityListSong.isAPIList()) {
            //set lyric
//            txt_lyric.setText(ActivityListSong.getSongListLyric().get(musicSrv.getSongPosn()).getLyric());
            img_disk.setVisibility(View.INVISIBLE);
        } else {
//            txt_lyric.setText("");
            img_disk.setVisibility(View.VISIBLE);
        }

        if (ActivityListSong.playbackPaused) {
            btn_play.setImageResource(R.mipmap.av_play);
        } else {
            btn_play.setImageResource(R.mipmap.av_pause);
            if (!ActivityListSong.isAPIList())
                img_disk.startAnimation(animation);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this, "onPause-player", Toast.LENGTH_SHORT).show();
        paused = true;

    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "onStop-player", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "onDestroy-player", Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        //animation
        animation = AnimationUtils.loadAnimation(this, R.anim.disk_rotate);

        //get data
        if (getIntent().getBundleExtra("bundle") != null) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            songEntity = (SongEntity) bundle.getSerializable("songEntity");
            posSong = getIntent().getBundleExtra("bundle").getInt("position");
        }
    }

    private void initControl() {
        txt_lyric = findViewById(R.id.txt_lyric);
        txt_song_name_player = findViewById(R.id.txt_song_name_player);
        txt_singer_player = findViewById(R.id.txt_singer_player);
        txt_time_song = findViewById(R.id.txt_time_song);
        txt_time_total = findViewById(R.id.txt_time_total);
        img_setting_player = findViewById(R.id.img_setting_player);
        btn_prev = findViewById(R.id.btn_prev);
        btn_play = findViewById(R.id.btn_play);
        btn_next = findViewById(R.id.btn_next);
        skb_player = findViewById(R.id.skb_player);
        img_disk = findViewById(R.id.img_disk_play);
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
                if (ActivityListSong.isMusicBound()) {
                    if (ActivityListSong.playbackPaused) {
                        musicSrv.start();
                        ActivityListSong.playbackPaused = false;
                        btn_play.setImageResource(R.mipmap.av_pause);
                        if (!ActivityListSong.isAPIList())
                            img_disk.startAnimation(animation);
                    } else {
                        musicSrv.pausePlayer();
                        ActivityListSong.playbackPaused = true;
                        btn_play.setImageResource(R.mipmap.av_play);
                        img_disk.clearAnimation();
                    }
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityListSong.isMusicBound()) {
                    musicSrv.playNext();
                    if (!ActivityListSong.isAPIList())
                        img_disk.startAnimation(animation);

                    //play button
                    if (ActivityListSong.playbackPaused) {
                        btn_play.setImageResource(R.mipmap.av_pause);
                        ActivityListSong.playbackPaused = false;
                    }
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityListSong.isMusicBound()) {
                    musicSrv.playPrev();
                    if (!ActivityListSong.isAPIList())
                        img_disk.startAnimation(animation);

                    if (ActivityListSong.playbackPaused) {
                        btn_play.setImageResource(R.mipmap.av_pause);
                        ActivityListSong.playbackPaused = false;
                    }
                }
            }
        });

        skb_player.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicSrv.seek(skb_player.getProgress());
            }
        });
    }

    public void setInfoSong(SongEntity songEntity) {
        txt_song_name_player.setText(songEntity.getSong_name());
        txt_singer_player.setText(songEntity.getSinger());
        txt_lyric.setText(songEntity.getLyric());
    }

    private void setTimeTotal() {
        if (musicSrv.isPng() && ActivityListSong.isMusicBound()) {
            txt_time_total.setText(milliSecondsToTimer((long) musicSrv.getDur()));
            skb_player.setMax(musicSrv.getDur());
        }
    }

    private void updateSongInterface() {
        skb_player.setProgress(0);
        if (handler == null) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInfoSong(musicSrv.getInfo());
                    setTimeTotal();
                    txt_time_song.setText(milliSecondsToTimer((long) musicSrv.getCurrentPosn()));
                    skb_player.setProgress(musicSrv.getCurrentPosn());

                    handler.postDelayed(this, 1000);
                }
            }, 100);
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static CircularImageView getImg_disk() {
        return img_disk;
    }
}
