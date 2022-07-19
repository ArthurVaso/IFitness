package br.edu.ifsp.dmo.ifitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class SportsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private DrawerLayout toolbarDrawer;

    private LinearLayout layoutIconWalk;
    private LinearLayout layoutIconRun;
    private LinearLayout layoutIconSwim;
    private LinearLayout layoutIconBike;

    private UserViewModel userViewModel;

    private UserWithActivities userWithActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

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
                if(userWithActivities != null){
                    SportsActivity.this.userWithActivities = userWithActivities;
                } else{
                    startActivity(new Intent(SportsActivity.this,
                            UserLoginActivity.class));
                    finish();
                }
            }
        });

        layoutIconWalk = findViewById(R.id.sport_icon_walk);
        layoutIconWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        SportRegisterActivity.class);
                intent.putExtra("title", getString(R.string.sports_walking));
                startActivity(intent);
            }
        });

        layoutIconWalk = findViewById(R.id.sport_icon_run);
        layoutIconWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        SportRegisterActivity.class);
                intent.putExtra("title", getString(R.string.sports_running));
                startActivity(intent);
            }
        });

        layoutIconWalk = findViewById(R.id.sport_icon_swim);
        layoutIconWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        SportRegisterActivity.class);
                intent.putExtra("title", getString(R.string.sports_swimming));
                startActivity(intent);
            }
        });

        layoutIconWalk = findViewById(R.id.sport_icon_bike);
        layoutIconWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        SportRegisterActivity.class);
                intent.putExtra("title", getString(R.string.sports_cycling));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }
}