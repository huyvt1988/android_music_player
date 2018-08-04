package com.example.alan.ntqmusicapp.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.app.Notification;
import android.app.PendingIntent;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.activity.ActivityPlayer;
import com.example.alan.ntqmusicapp.room.SongEntity;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer player;
    private List<SongEntity> songList;
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();
    private SongEntity playingSong;
    //notification id
    private static final int NOTIFY_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        songPosn = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }


    public void setList(List<SongEntity> theSongs) {
        songList = theSongs;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong() {
        player.reset();
        playingSong = songList.get(songPosn);

        long currSong = playingSong.getId_long();
        Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
            player.prepareAsync();
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Intent notIntent = new Intent(this, ActivityPlayer.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.music_icon);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        builder.setContentIntent(pendInt)
                .setSmallIcon(Icon.createWithBitmap(bitmap))
                .setLargeIcon(bitmap)
                .setTicker(playingSong.getSong_name())
                .setOngoing(true)
                .setContentTitle(playingSong.getSong_name())
                .setContentText(playingSong.getSinger());
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public int getCurrentPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void start() {
        player.start();
    }

    public void playPrev() {
        songPosn--;
        if (songPosn < 0)
            songPosn = songList.size() - 1;
        playSong();
    }

    public void playNext() {
        songPosn++;
        if (songPosn >= songList.size())
            songPosn = 0;
        playSong();
    }

    public void stopPlayer() {
        player.stop();
        player.release();
    }

    public SongEntity getInfo() {
        if (playingSong != null)
            return playingSong;
        else
            return songList.get(songPosn);
    }

    public int getSongPosn() {
        return songPosn;
    }

}
