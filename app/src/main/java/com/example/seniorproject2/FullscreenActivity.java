package com.example.seniorproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.io.IOException;
import java.util.Objects;

public class FullscreenActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "anti-clicker";
    private final MediaPlayer menuMusic = new MediaPlayer();
    public boolean musicIsMuted = false;
    public boolean sfxIsMuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.main_menu);
        prefs = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        ImageView imageView = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.anticlicker);
        imageView.setImageBitmap(bitmap);
        Button button = findViewById(R.id.playButton);
        button.setOnClickListener(this::gameStart);
        Button button2 = findViewById(R.id.settingsButton);
        button2.setOnClickListener(view -> settingsBox());
        Button button3 = findViewById(R.id.quitButton);
        button3.setOnClickListener(view -> finishAffinity());
        loadMusic();
    }

    //starts the game by calling updateScore and changing activities to the game activity
    public void gameStart(View view) {
        updateScore();
        Intent intent = new Intent(FullscreenActivity.this, GameActivity2.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //shows settings box, and sets buttons and sliders
    @SuppressLint("ClickableViewAccessibility")
    private void settingsBox() {
        //opens settings box and disables main menu buttons
        Button button = findViewById(R.id.playButton);
        Button button2 = findViewById(R.id.settingsButton);
        Button button3 = findViewById(R.id.quitButton);
        button.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        PopupWindow popupWindow = new PopupWindow(this);
        View contentView = getLayoutInflater().inflate(R.layout.popup_settings, null);
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        ConstraintLayout mainMenu = findViewById(R.id.mainMenu);


        //allows settings box to be closed via touch outside the box and re-enables main menu buttons
        popupWindow.setOnDismissListener(popupWindow::dismiss);
        mainMenu.setOnTouchListener((v, event) -> {
            if (popupWindow.isShowing()) {
                // Closes the settings box
                editor.putBoolean("musicIsMuted", musicIsMuted);
                editor.apply();
                editor.putBoolean("sfxIsMuted", sfxIsMuted);
                editor.apply();
                button.setClickable(true);
                button2.setClickable(true);
                button3.setClickable(true);
                popupWindow.dismiss();
            }
            return false;
        });

        //enables button to mute music
        Button musicMuteButton = contentView.findViewById(R.id.muteMusicButton);
        musicMuteButton.setOnClickListener(view -> {
            if (!musicIsMuted) {
                musicIsMuted = true;
                menuMusic.pause();
            }
            else {
                musicIsMuted = false;
                menuMusic.start();
            }
        });

        //enables button to mute sound effects
        Button sfxButton = contentView.findViewById(R.id.muteSoundEffectButton);
        sfxButton.setOnClickListener(view -> {
            if (!sfxIsMuted) {
                sfxIsMuted=true;
            }
            else {
                sfxIsMuted=false;
            }
        });
    }

    private void loadMusic() {
        try {
            AssetFileDescriptor afd = getAssets().openFd("anti_clicker_menu.mp3");
            menuMusic.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            menuMusic.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuMusic.setLooping(true);
        menuMusic.start();
    }

    //increments the score based on time when in menu
    private void updateScore() {
        long score = prefs.getLong("score", 100L);
        long lastPlayedTime = prefs.getLong("last_played_time", System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastPlayedTime;
        editor.putLong("timeDifference", timeDifference / 1000L);
        editor.putLong("score", score);
        editor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();
        musicIsMuted = prefs.getBoolean("musicIsMuted", musicIsMuted);
        if(musicIsMuted){
            menuMusic.pause();
        } else { menuMusic.start(); }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(menuMusic.isPlaying()) {
           menuMusic.stop();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(menuMusic.isPlaying()){
            menuMusic.stop();
            menuMusic.release();
        }
    }

    //hides the system's action bar and creates a fullscreen activity (called by onCreate())
    private void hideSystemUI() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController cont = getWindow().getInsetsController();
            if (cont != null) {
                cont.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                cont.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }
}