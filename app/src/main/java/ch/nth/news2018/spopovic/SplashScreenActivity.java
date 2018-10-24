package ch.nth.news2018.spopovic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.nth.news2018.spopovic.home.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                // close splash activity
                finish();
            }
        }, SPLASH_SCREEN_DELAY);

    }
}
