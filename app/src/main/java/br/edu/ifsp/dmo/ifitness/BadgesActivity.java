package br.edu.ifsp.dmo.ifitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class BadgesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextView points;
    private LinearLayout noBadges;
    private LinearLayout badgeBeginner;
    private LinearLayout badgeBronze;
    private LinearLayout badgeSilver;
    private LinearLayout badgeGold;
    private LinearLayout badgePlatinum;
    private double userPoints;

    private String userId;

    private UserViewModel userViewModel;

    private UserWithActivities userWithActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.badges_title);

        points = findViewById(R.id.badges_txt_points);

        noBadges = findViewById(R.id.no_badges);
        badgeBeginner = findViewById(R.id.badge_beginner);
        badgeBronze = findViewById(R.id.badge_bronze);
        badgeSilver = findViewById(R.id.badge_silver);
        badgeGold = findViewById(R.id.badge_gold);
        badgePlatinum = findViewById(R.id.badge_platinum);

        userViewModel.islogged().observe(this, new Observer<UserWithActivities>() {
            @Override
            public void onChanged(UserWithActivities userWithActivities) {
                if (userWithActivities != null) {

                    BadgesActivity.this.userWithActivities = userWithActivities;
                    userPoints = Double.parseDouble(userWithActivities.getUser().getPoints());
                    setUnsetBadges(userPoints / 1000);
                } else {
                    startActivity(new Intent(BadgesActivity.this,
                            UserLoginActivity.class));
                    finish();
                }
            }
        });
    }

    private void setUnsetBadges(double userPoints) {
        String txtPoints = getString(R.string.badges_your_points)
                + String.valueOf(userPoints)
                + getString(R.string.badges_km);
        points.setText(txtPoints);

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