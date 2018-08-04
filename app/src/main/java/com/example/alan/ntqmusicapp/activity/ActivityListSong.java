package com.example.alan.ntqmusicapp.activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.alan.ntqmusicapp.controller.APILyricAsyncTask;
import com.example.alan.ntqmusicapp.model.ItemClickListener;
import com.example.alan.ntqmusicapp.room.AppDatabase;
import com.example.alan.ntqmusicapp.room.DataGenerator;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.example.alan.ntqmusicapp.service.MusicService;
import com.example.alan.ntqmusicapp.service.MusicService.MusicBinder;
import com.squareup.picasso.Picasso;

import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityListSong extends MyActivity {
    private ImageView img_setting_list, img_song_list;
    private Button btn_sort_by_name, btn_sort_by_folder, btn_sort_by_api;
    private ImageButton btn_prev_mini, btn_play_mini, btn_next_mini;
    private RecyclerView revw_song;
    private TextView txt_song_name_list, txt_singer_list;

    private AppDatabase database;
    private List<SongEntity> songList;
    private List<SongEntity> songListFolder;
    private static List<SongEntity> songListLyric;

    private SongAdapter songAdapter;

    private static MusicService musicSrv;
    private Intent playIntent;
    private static boolean musicBound = false;

    public static boolean paused = false, playbackPaused = true;
    private static boolean isAPIList = false;

    public static final String API_LYRIC = "https://raw.githubusercontent.com/MrNinja/android_music_app_api/master/api/lyric/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.lt_list_song);
        startService();
        initData();
        initControl();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        paused = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        if (musicBound) {
            this.unbindService(musicConnection);
//            Toast.makeText(ActivityListSong.this, "Service Un-Binded", Toast.LENGTH_SHORT).show();
        }
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
                if (isAPIList)
                    isAPIList = false;
                songList = DataGenerator.with(database).getSongsByName();
                if (songList.size() > 0) {
                    setSongListApp(songList);
                }
            }
        });

        btn_sort_by_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAPIList)
                    isAPIList = false;
                songList = DataGenerator.with(database).getSongsByFolder();
                if (songList.size() > 0) {
                    setSongListApp(songList);
                }
            }
        });

        btn_sort_by_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAPIList)
                    isAPIList = true;
                if (songListLyric.size() > 0) {
                    setSongListApp(songListLyric);
                } else {
                    setRecycleViewData(songListLyric);
                }
            }
        });

        btn_next_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBound) {
                    musicSrv.playNext();
                    setInfoSong(musicSrv.getInfo());
                    if (playbackPaused) {
                        playbackPaused = false;
                        btn_play_mini.setImageResource(R.mipmap.av_pause);
                    }
                }
            }
        });
        btn_prev_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBound) {
                    musicSrv.playPrev();
                    setInfoSong(musicSrv.getInfo());
                    if (playbackPaused) {
                        playbackPaused = false;
                        btn_play_mini.setImageResource(R.mipmap.av_pause);
                    }
                }
            }
        });
        btn_play_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBound) {
                    if (playbackPaused) {
                        musicSrv.start();
                        setInfoSong(musicSrv.getInfo());
                        playbackPaused = false;
                        btn_play_mini.setImageResource(R.mipmap.av_pause);
                    } else {
                        musicSrv.pausePlayer();
                        playbackPaused = true;
                        btn_play_mini.setImageResource(R.mipmap.av_play);
                    }
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
        //get song list by name
        songList = DataGenerator.with(database).getSongsByName();

        //get song list by folder
        songListFolder = DataGenerator.with(database).getSongsByFolder();

        //get API list
        songListLyric = DataGenerator.with(database).getSongsByAPI();
        for (int i = 0; i < songListLyric.size(); i++) {
            SongEntity songEntity = songListLyric.get(i);
            APILyricAsyncTask asyncTask = new APILyricAsyncTask(songEntity);
            asyncTask.execute(API_LYRIC + songEntity.getId());
            songEntity.setImage_url(getListImageURL().get(i));
        }
    }

    private void initControl() {
        img_setting_list = findViewById(R.id.img_setting_list);
        img_song_list = findViewById(R.id.img_song_list);
        btn_sort_by_name = findViewById(R.id.btn_sort_by_name);
        btn_sort_by_folder = findViewById(R.id.btn_sort_by_folder);
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

                //play song
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
        Picasso.with(this).load(songEntity.getImage_url())
                .placeholder(R.mipmap.icon_song).error(R.mipmap.loading).into(img_song_list);
    }

    public void setSongListApp(List<SongEntity> songList) {
            musicSrv.setList(songList);
            setRecycleViewData(songList);
            setEventAdapter();
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
//                Toast.makeText(ActivityListSong.this, "bindService", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<String> getListImageURL() {
        List<String> lisURL = new ArrayList<>();
        lisURL.add("https://i.ytimg.com/vi/2MqYLezplqQ/default.jpg");
        lisURL.add("https://avatar-nct.nixcdn.com/song/2018/03/10/7/0/c/b/1520688037080_640.jpg");
        lisURL.add("https://d2tml28x3t0b85.cloudfront.net/tracks/artworks/000/348/988/original/d9a0a2.jpeg?1474466250");
        lisURL.add("http://hocdanpiano.vn/wp-content/uploads/2015/09/816669.jpg");
        lisURL.add("http://kenhnhaccho.com/thumb/resize/w/200/h/200/z/1/url.kenhnhac.vn/res/AVATAR/SONG/2018/3/20180327101240488_1.jpg");
        lisURL.add("http://kenhnhaccho.com/thumb/resize/w/200/h/200/z/1/url.kenhnhac.vn/res/AVATAR/SONG/2018/3/20180327101240488_1.jpg");
        lisURL.add("http://v2.cdn.nhac.vn/kv0puCNE4oNNfn7YhOpK/1489548494/v1/album/s1/0/0/26/27427.jpg");
        lisURL.add("http://data.chiasenhac.com/data/cover/1/331.jpg");
        lisURL.add("https://avatar-nct.nixcdn.com/song/2018/04/20/e/d/8/5/1524195310927_640.jpg");
        lisURL.add("https://avatar-nct.nixcdn.com/song/2018/04/20/e/d/8/5/1524195406609_640.jpg");

        return lisURL;
    }

    public static List<SongEntity> getSongListLyric() {
        return songListLyric;
    }

    public static MusicService getMusicSrv() {
        return musicSrv;
    }

    public static boolean isMusicBound() {
        return musicBound;
    }

    public static boolean isAPIList() {
        return isAPIList;
    }
}
