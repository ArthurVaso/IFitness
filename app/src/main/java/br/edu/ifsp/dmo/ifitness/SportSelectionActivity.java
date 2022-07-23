package br.edu.ifsp.dmo.ifitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class SportSelectionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private DrawerLayout toolbarDrawer;

    private LinearLayout layoutIconWalk;
    private LinearLayout layoutIconRun;
    private LinearLayout layoutIconSwim;
    private LinearLayout layoutIconBike;

    private UserViewModel userViewModel;

    private UserWithActivities userWithActivities;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_selection);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.sports_title));

        userViewModel.islogged().observe(this, new Observer<UserWithActivities>() {
            @Override
            public void onChanged(UserWithActivities userWithActivities) {
                if (userWithActivities != null) {
                    SportSelectionActivity.this.userWithActivities = userWithActivities;
                    userId = userWithActivities.getUser().getId();
                } else {
                    startActivity(new Intent(SportSelectionActivity.this,
                            UserLoginActivity.class));
                    finish();
                }
            }
        });

        layoutIconWalk = findViewById(R.id.sport_selection_icon_walk);
        layoutIconWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSportListActivity(getString(R.string.kind_walking), getString(R.string.sports_walking));
            }
        });

        layoutIconRun = findViewById(R.id.sport_selection_icon_run);
        layoutIconRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSportListActivity(getString(R.string.kind_running), getString(R.string.sports_running));
            }
        });

        layoutIconSwim = findViewById(R.id.sport_selection_icon_swim);
        layoutIconSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSportListActivity(getString(R.string.kind_swimming), getString(R.string.sports_swimming));
            }
        });

        layoutIconBike = findViewById(R.id.sport_selection_icon_bike);
        layoutIconBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSportListActivity(getString(R.string.kind_cycling), getString(R.string.sports_cycling));
            }
        });
    }

    private void callSportListActivity(String sportType, String title) {
        Intent intent = new Intent(SportSelectionActivity.this,
                SportListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("sportType", sportType);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}