package br.edu.ifsp.dmo.ifitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BadgesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private String title;
    
    private TextView points;
    private LinearLayout noBadges;
    private LinearLayout badgeBeginner;
    private LinearLayout badgeBronze;
    private LinearLayout badgeSilver;
    private LinearLayout badgeGold;
    private LinearLayout badgePlatinum;
    private double userPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        //title = intent.getStringExtra("title").toString();

        toolbarTitle = findViewById(R.id.toolbar_title);
        //toolbarTitle.setText(title);
        
        points = findViewById(R.id.badges_txt_points);
        String txtPoints = getString(R.string.badges_your_points)
                + String.valueOf(userPoints)
                + getString(R.string.badges_km);
        points.setText(txtPoints);
        Log.d("pontos", "onCreate: " + txtPoints);
        Log.d("pontos", "onCreate: " + getString(R.string.badges_your_points));
        Log.d("pontos", "onCreate: " + userPoints);
        Log.d("pontos", "onCreate: " + getString(R.string.badges_km));
        noBadges = findViewById(R.id.no_badges);
        badgeBeginner = findViewById(R.id.badge_beginner);
        badgeBronze = findViewById(R.id.badge_bronze);
        badgeSilver = findViewById(R.id.badge_silver);
        badgeGold = findViewById(R.id.badge_gold);
        badgePlatinum = findViewById(R.id.badge_platinum);
        
        if (userPoints > 15) {
            noBadges.setVisibility(View.INVISIBLE);
            badgeBeginner.setVisibility(View.VISIBLE);
        } else {
            noBadges.setVisibility(View.VISIBLE);
            badgeBeginner.setVisibility(View.INVISIBLE);
        }

        if (userPoints > 25) {
            badgeBronze.setVisibility(View.VISIBLE);
        } else {
            badgeBronze.setVisibility(View.INVISIBLE);
        }

        if (userPoints > 50) {
            badgeSilver.setVisibility(View.VISIBLE);
        } else {
            badgeSilver.setVisibility(View.INVISIBLE);
        }

        if (userPoints > 100) {
            badgeGold.setVisibility(View.VISIBLE);
        } else {
            badgeGold.setVisibility(View.INVISIBLE);
        }

        if (userPoints > 150) {
            badgePlatinum.setVisibility(View.VISIBLE);
        } else {
            badgePlatinum.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}