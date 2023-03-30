package com.example.seniorproject2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private long score;
    //private long autoScore;
    private long decAutoScore;
    private long clickMultiplier = 1;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "anti-clicker";
    private long decrementSec = 1000;
    private final MediaPlayer gameMusic = new MediaPlayer();
    private final MediaPlayer perilDrums = new MediaPlayer();

    public static int gameMusicPos = 0;
    public static float gameMusicVol = 100f;
    private SoundPool popSFX = new SoundPool.Builder().setMaxStreams(10).build();
    private int popID;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_fullscreen);
        prefs = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        loadAudio();
        //set score based on whether the game has been played before
        TextView scoreText = findViewById(R.id.score);
        if (prefs.contains("first_time")) {
            // Not the first time opening the app
            score = prefs.getLong("score", score);
            scoreText.setText(Long.toString(score));
        } else {
            // First time opening the app
            score = 100L;
            scoreText.setText(Long.toString(score));
            editor.putBoolean("first_time", true);
            editor.apply();
        }
        //enables clicking on the main screen to increase score
        ConstraintLayout constraintLayout = findViewById(R.id.mainScreen);
        constraintLayout.setOnClickListener(v -> {
            score+=clickMultiplier;
            popSFX.play(popID, 1.0f, 1.0f, 1, 0, 1.0f);
            scoreText.setText(String.valueOf(score));
            decrementSec--;
        });
        //enables vertical mobility of upgrades list with the bottom bar
        RelativeLayout upgradesList = findViewById(R.id.upgradesList);
        ImageView imageView = findViewById(R.id.bottom_bar);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            float dY=5;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dY = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float newY = event.getRawY() + dY;
                        if (newY < scoreText.getBottom()) {
                            newY = scoreText.getBottom();
                            upgradesList.setVisibility(View.GONE);
                        }
                        upgradesList.setVisibility(View.VISIBLE);
                        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
                        int screenHeight = displayMetrics.heightPixels;
                        if (newY + view.getHeight() > screenHeight) {
                            newY = screenHeight - view.getHeight();
                        }
                        float finalNewY = newY;
                        imageView.post(() -> imageView.animate()
                                .y(finalNewY)
                                .setDuration(0)
                                .start());
                        upgradesList.setY(finalNewY+imageView.getHeight());
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        //enables the back button
        ImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(this::back);
        //enables decrement
        Timer timer = new Timer();
        timer.schedule(new scoreDecrement(), decrementSec);

        loadUpgrades();
    }

    private void loadUpgrades() {
        Upgrade upgrade1 = new Upgrade(1, 1, 10, 0, "y=1*power+2", "y=2*price");
        UpgradesDatabase upgradeDB = new UpgradesDatabase();
        upgradeDB.addUpgrade(upgrade1);
    }

    //enables decrement of score over time
    private class scoreDecrement extends TimerTask {
        @Override
        public void run(){
            score=score-decAutoScore;
            runOnUiThread(() -> {
                TextView scoreText = findViewById(R.id.score);
                scoreText.setText(String.valueOf(score));
            });
        }
    };

    //restores the state of all variables listed
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            score = savedInstanceState.getLong("score", 100L);
        }
    }

    //saves the state of all variables listed
    @Override
    protected void onSaveInstanceState (@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("score", score);
    }

    //stores the variables listed to be used in the main menu
    @Override
    protected void onPause() {
        super.onPause();
        editor.putLong("score", score);
        editor.putLong("last_played_time", System.currentTimeMillis());
        editor.apply();
        gameMusicPos = gameMusic.getCurrentPosition();
        gameMusic.pause();
        perilDrums.pause();
    }

    //same as onPause(), but also stops the music
    @Override
    protected void onStop() {
        super.onStop();
        editor.putLong("score", score);
        editor.putLong("last_played_time", System.currentTimeMillis());
        editor.apply();
        gameMusicPos = gameMusic.getCurrentPosition();
        gameMusic.release();
        perilDrums.release();
    }

    //grabs the score from the main menu
    @Override
    protected void onResume() {
        super.onResume();
        score = prefs.getLong("score", 100L);
        loadAudio();
    }

    //hides the system's action bar and creates a fullscreen activity (called by onCreate())
    private void hideSystemUI() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController cont = getWindow().getInsetsController();
            if (cont != null){
                cont.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                cont.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }

    //stores the variables listed and changes to the main menu activity
    public void back(View view) {
        editor.putLong("score", score);
        editor.putLong("last_played_time", System.currentTimeMillis());
        editor.apply();
        Intent intent = new Intent(GameActivity.this, FullscreenActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void loadAudio() {
        //starts music and enables low-score music
        try {
            AssetFileDescriptor afd = getAssets().openFd("anti_clicker.wav");
            gameMusic.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            gameMusic.prepare();
            afd = getAssets().openFd("anti_clicker_2.wav");
            perilDrums.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            perilDrums.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gameMusic.setLooping(true);
        perilDrums.setLooping(true);
        gameMusic.seekTo(FullscreenActivity.menuMusicPos);
        perilDrums.seekTo(FullscreenActivity.menuMusicPos);
        float musicVol = prefs.getFloat("musicVol", 100);
        boolean musicIsMuted = prefs.getBoolean("musicIsMuted", false);
        if(musicIsMuted) {
            gameMusic.setVolume(0,0);
        } else { gameMusic.setVolume(musicVol, musicVol); }
        perilDrums.setVolume(0,0);
        gameMusic.start();
        while(score<100){
            perilDrums.setVolume(Math.abs(score-100), Math.abs(score-100));
        }

        //enables sound effects
        popID = popSFX.load(this, R.raw.pop, 1);
        boolean sfxIsMuted = prefs.getBoolean("sfxIsMuted", true);
        if(!sfxIsMuted) {
            float popVol = prefs.getFloat("popVol", 100);
            popSFX.setVolume(popID, popVol, popVol);

        } else {
            popSFX.setVolume(popID, 0, 0);
        }
    }
}
