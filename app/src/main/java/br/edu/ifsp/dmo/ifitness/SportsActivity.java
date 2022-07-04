package br.edu.ifsp.dmo.ifitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class SportsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private DrawerLayout toolbarDrawer;

    private LinearLayout layoutIconWalk;
    private LinearLayout layoutIconRun;
    private LinearLayout layoutIconSwim;
    private LinearLayout layoutIconBike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.sports_title));

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