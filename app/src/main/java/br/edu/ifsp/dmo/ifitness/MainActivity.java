package br.edu.ifsp.dmo.ifitness;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import br.edu.ifsp.dmo.ifitness.adapter.ActivityAdapter;
import br.edu.ifsp.dmo.ifitness.model.PhysicalActivities;
import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private DrawerLayout toolbarDrawer;

    private NavigationView navigationView;
    private TextView txtLogin;

    private ImageView profileImage;

    private UserViewModel userViewModel;
    private RecyclerView recyclerActivities;
    private ActivityAdapter activityAdapter;

    private UserWithActivities userWithActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.app_name));

        toolbarDrawer = findViewById(R.id.nav_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                toolbarDrawer, toolbar, R.string.toggle_open, R.string.toggle_close);
        toolbarDrawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = findViewById(R.id.menu_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(MainActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_user:
                        intent = new Intent(MainActivity.this,
                                UserProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_activities:
                        intent = new Intent(MainActivity.this,
                                SportsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_selection:
                        intent = new Intent(MainActivity.this,
                                SportSelectionActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_leaderboard:
                        intent = new Intent(MainActivity.this,
                                LeaderboardActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_badges:
                        intent = new Intent(MainActivity.this,
                                BadgesActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        userViewModel.logout();
                        finish();
                        startActivity(getIntent());
                        break;
                }

                toolbarDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        txtLogin = navigationView.getHeaderView(0)
                .findViewById(R.id.header_profile_name);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        UserLoginActivity.class);
                startActivity(intent);
            }
        });

        profileImage = navigationView.getHeaderView(0)
                .findViewById(R.id.header_profile_image);

        recyclerActivities = findViewById(R.id.main_recycler_activities);

        activityAdapter = new ActivityAdapter(this);
        recyclerActivities.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        userViewModel.islogged().observe(this,
                new Observer<UserWithActivities>() {
                    @Override
                    public void onChanged(UserWithActivities userWithActivities) {
                        if (userWithActivities != null) {
                            txtLogin.setText(new StringBuilder()
                                    .append(userWithActivities.getUser().getName())
                                    .append(" ")
                                    .append(userWithActivities.getUser().getSurname())
                                    .toString());
                            String imageProfile = PreferenceManager
                                    .getDefaultSharedPreferences(MainActivity.this)
                                    .getString(MediaStore.EXTRA_OUTPUT, null);
                            if (imageProfile != null) {
                                profileImage.setImageURI(Uri.parse(imageProfile));
                            } else {
                                profileImage.setImageResource(R.drawable.profile_image);
                            }

                            MainActivity.this.userWithActivities = userWithActivities;
                            userViewModel = new ViewModelProvider(MainActivity.this)
                                    .get(UserViewModel.class);

                            activityAdapter = new ActivityAdapter(MainActivity.this);

                            userViewModel.recentActivities().observe(MainActivity.this,
                                    new Observer<List<PhysicalActivities>>() {
                                        @Override
                                        public void onChanged(List<PhysicalActivities> physicalActivities) {
                                            activityAdapter.setActivities(physicalActivities);
                                            activityAdapter.notifyDataSetChanged();
                                        }
                                    });

                            recyclerActivities.setAdapter(activityAdapter);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (toolbarDrawer.isDrawerOpen(GravityCompat.START)) {
            toolbarDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}