package com.mypj.musicplayeroffline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayingScreen extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    ImageButton imageButtonPlay, imageButtonPrevious, imageButtonNext;
    TextView textViewSongName, textViewCurrentTime, textViewEndTime;
    MediaPlayer mediaPlayer;
    ArrayList<File> songs;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playing_screen);
        imageButtonPlay = findViewById(R.id.imageButtonPlay);
        imageButtonNext = findViewById(R.id.imageButtonNext);
        imageButtonPrevious = findViewById(R.id.imageButtonPrevious);
        textViewSongName = findViewById(R.id.textViewSongName);
        textViewCurrentTime = findViewById(R.id.textViewCurentTime);
        textViewEndTime = findViewById(R.id.textViewEndTime);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songsList");
        textContent = intent.getStringExtra("currentSong");
        textViewSongName.setText(textContent);
        textViewSongName.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(mediaPlayer.getDuration());
                String totalTime = createTimeLabel(mediaPlayer.getDuration());
                textViewEndTime.setText(totalTime);
                mediaPlayer.start();
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        String currentTime = createTimeLabel(i);
                        textViewCurrentTime.setText(currentTime);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                });
            }
        });
        mediaPlayer.setLooping(true);
        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();



        imageButtonPlay.setImageResource(R.drawable.pause);
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    imageButtonPlay.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                } else {
                    imageButtonPlay.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != 0) {
                    position = position - 1;
                } else {
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                textContent = songs.get(position).getName();
                textViewSongName.setText(textContent);
                imageButtonPlay.setImageResource(R.drawable.pause);
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                textContent = songs.get(position).getName();
                textViewSongName.setText(textContent);
                imageButtonPlay.setImageResource(R.drawable.pause);
            }
        });
    }
    public String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;


    }
}