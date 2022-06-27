package br.edu.ifsp.dmo.ifitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SportsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private DrawerLayout toolbarDrawer;

    private LinearLayout layoutIcomWalk;
    private LinearLayout layoutIcomRun;
    private LinearLayout layoutIcomSwim;
    private LinearLayout layoutIcomBike;

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

        layoutIcomWalk = findViewById(R.id.sport_icon_walk);
        layoutIcomWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        DetailSportActivity.class);
                intent.putExtra("title", getString(R.string.walking));
                startActivity(intent);
            }
        });

        layoutIcomWalk = findViewById(R.id.sport_icon_run);
        layoutIcomWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        DetailSportActivity.class);
                intent.putExtra("title", getString(R.string.running));
                startActivity(intent);
            }
        });

        layoutIcomWalk = findViewById(R.id.sport_icon_swim);
        layoutIcomWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        DetailSportActivity.class);
                intent.putExtra("title", getString(R.string.swimming));
                startActivity(intent);
            }
        });

        layoutIcomWalk = findViewById(R.id.sport_icon_bike);
        layoutIcomWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SportsActivity.this,
                        DetailSportActivity.class);
                intent.putExtra("title", getString(R.string.cycling));
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