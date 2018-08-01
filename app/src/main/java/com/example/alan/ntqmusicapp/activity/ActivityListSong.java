package com.example.alan.ntqmusicapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.adapter.SongAdapter;
import com.example.alan.ntqmusicapp.controller.LyricAsyncTask;
import com.example.alan.ntqmusicapp.entity.SongLyric;
import com.example.alan.ntqmusicapp.model.ItemClickListener;
import com.example.alan.ntqmusicapp.room.AppDatabase;
import com.example.alan.ntqmusicapp.room.DataGenerator;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.example.alan.ntqmusicapp.service.MusicService;
import com.example.alan.ntqmusicapp.service.MusicService.MusicBinder;

import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityListSong extends AppCompatActivity {
    private ImageView img_setting_list, img_song_list;
    private Button btn_sort_by_name, btn_sort_by_api;
    private ImageButton btn_prev_mini, btn_play_mini, btn_next_mini;
    private RecyclerView revw_song;
    private TextView txt_song_name_list, txt_singer_list;

    AppDatabase database;
    private static List<SongEntity> songList;
    public static List<SongLyric> songLyricList;
    private SongAdapter songAdapter;

    public static MusicService musicSrv;
    private Intent playIntent;
    public static boolean musicBound = false;

    public static boolean paused = false, playbackPaused = true;
    private SongEntity songEntity;
    private boolean isBackGround = false;
    public static boolean isAPIList = false;

    public static final String API_LYRIC = "https://raw.githubusercontent.com/MrNinja/android_music_app_api/master/api/lyric/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.lt_list_song);
        startService();
        initData();
        initControl();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        if (paused) {
            paused = false;
        }

        //update status mini player
        if (musicBound) {
            setInfoSong(musicSrv.getInfo());
        }
        if (playbackPaused) {
            btn_play_mini.setImageResource(R.mipmap.av_play);
        } else {
            btn_play_mini.setImageResource(R.mipmap.av_pause);
        }

        //run on background
        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        isBackGround = sharedPreferences.getBoolean("isBackGround", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        paused = true;

        //background
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        if (musicBound) {
            this.unbindService(musicConnection);
            Toast.makeText(ActivityListSong.this, "Service Un-Binded", Toast.LENGTH_SHORT).show();
        }
//        this.stopService(playIntent);
    }

    //connect to the service
    public ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void initEvent() {
        img_setting_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityListSong.this, ActivitySetting.class);
                startActivity(intent);
            }
        });

        btn_sort_by_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAPIList = false;
                sort_by_name();
                songList = DataGenerator.with(database).getSongsByName();
                if (songList.size() > 0) {
                    musicSrv.setList(songList);
                    setRecycleViewData(songList);
                    setEventAdapter();
                }
            }
        });


        btn_sort_by_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAPIList = true;
                songList = DataGenerator.with(database).getSongsByAPI();
                if (songList.size() > 0) {
                    musicSrv.setList(songList);
                    setRecycleViewData(songList);
                    setEventAdapter();
                } else {
                    setRecycleViewData(songList);
                }
            }
        });

        btn_next_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext();
                songEntity = musicSrv.getInfo();
                setInfoSong(songEntity);
                if (playbackPaused) {
                    playbackPaused = false;
                    btn_play_mini.setImageResource(R.mipmap.av_pause);
                }
            }
        });
        btn_prev_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();
                songEntity = musicSrv.getInfo();
                setInfoSong(songEntity);
                if (playbackPaused) {
                    playbackPaused = false;
                    btn_play_mini.setImageResource(R.mipmap.av_pause);
                }
            }
        });
        btn_play_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playbackPaused) {
                    musicSrv.start();
                    songEntity = musicSrv.getInfo();
                    setInfoSong(songEntity);
                    playbackPaused = !playbackPaused;
                    btn_play_mini.setImageResource(R.mipmap.av_pause);
                } else {
                    musicSrv.pausePlayer();
                    playbackPaused = !playbackPaused;
                    btn_play_mini.setImageResource(R.mipmap.av_play);
                }
            }
        });

        songAdapter.setOnClickListener(new ItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                Intent intent = new Intent(ActivityListSong.this, ActivityPlayer.class);
                SongEntity songEntity = songList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("songEntity", songEntity);
                bundle.putInt("position", position);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

                //play
                songPicked(position);
            }
        });

        setEventAdapter();
    }

    private void initData() {
        database = AppDatabase.getAppDatabase(this);
        songList = DataGenerator.with(database).getSongsByName();

        //get lyric list
        songLyricList = new ArrayList<>();
        for (int i = 0; i < DataGenerator.with(database).getSongsByAPI().size(); i++) {
            SongLyric songLyric = new SongLyric();
            songLyric.setId(DataGenerator.with(database).getSongsByAPI().get(i).getId());
            LyricAsyncTask asyncTask = new LyricAsyncTask(songLyric);
            asyncTask.execute(API_LYRIC + DataGenerator.with(database).getSongsByAPI().get(i).getId());
            songLyricList.add(songLyric);
        }
    }

    private void initControl() {
        img_setting_list = findViewById(R.id.img_setting_list);
        img_song_list = findViewById(R.id.img_song_list);
        btn_sort_by_name = findViewById(R.id.btn_sort_by_name);
        btn_sort_by_api = findViewById(R.id.btn_sort_by_api);
        btn_prev_mini = findViewById(R.id.btn_prev_mini);
        btn_play_mini = findViewById(R.id.btn_play_mini);
        btn_next_mini = findViewById(R.id.btn_next_mini);
        revw_song = findViewById(R.id.revw_song);
        txt_song_name_list = findViewById(R.id.txt_song_name_list);
        txt_singer_list = findViewById(R.id.txt_singer_list);

        revw_song.setLayoutManager(new LinearLayoutManager(this));
        revw_song.setItemAnimator(new DefaultItemAnimator());

        setRecycleViewData(songList);

    }

    public void setEventAdapter() {
        songAdapter.setOnClickListener(new ItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                Intent intent = new Intent(ActivityListSong.this, ActivityPlayer.class);
                SongEntity songEntity = songList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("songEntity", songEntity);
                bundle.putInt("position", position);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

                //play
                songPicked(position);
            }
        });
    }

    public void setRecycleViewData(List<SongEntity> songList) {
        songAdapter = new SongAdapter(this, songList);
        revw_song.setAdapter(songAdapter);
    }

    public void setInfoSong(SongEntity songEntity) {
        txt_song_name_list.setText(songEntity.getSong_name());
        txt_singer_list.setText(songEntity.getSinger());
    }

    public void sort_by_name() {
        Collections.sort(songList, new Comparator<SongEntity>() {
            public int compare(SongEntity a, SongEntity b) {
                return a.getSong_name().compareTo(b.getSong_name());
            }
        });
    }

    public void songPicked(int pos) {
        musicSrv.setSong(pos);
        musicSrv.playSong();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }

    public void startService() {
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            if (!musicBound) {
                bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
                Toast.makeText(ActivityListSong.this, "bindService", Toast.LENGTH_SHORT).show();
            }
//            startService(playIntent);
        }
    }
}
