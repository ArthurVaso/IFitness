package br.edu.ifsp.dmo.ifitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.dmo.ifitness.adapter.ActivityAdapter;
import br.edu.ifsp.dmo.ifitness.model.PhysicalActivities;
import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class SportListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private String title;

    private String sportType;
    private String userId;

    private UserViewModel userViewModel;

    private RecyclerView recyclerActivities;
    private ActivityAdapter activityAdapter;

    private UserWithActivities userWithActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_list);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Intent intent = getIntent();
        //title = intent.getStringExtra("title");
        //sportType = intent.getStringExtra("sportType");
        //userId = intent.getStringExtra("userId");
        title = getString(R.string.sport_list_title);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);

        recyclerActivities = findViewById(R.id.sport_list_recycler_activities);
        //Log.d("frag", "onChanged: userview");
        userViewModel = new ViewModelProvider(SportListActivity.this)
                .get(UserViewModel.class);

        //Log.d("frag", "onChanged: adapter");
        recyclerActivities.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false));

        activityAdapter = new ActivityAdapter(SportListActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        userViewModel.islogged().observe(SportListActivity.this, new Observer<UserWithActivities>() {
            @Override
            public void onChanged(UserWithActivities userWithActivities) {
                Log.d("sedAct", "onChanged do islogged: ");
                if (userWithActivities != null) {
                    SportListActivity.this.userWithActivities = userWithActivities;
                    userId = SportListActivity.this.userWithActivities.getUser().getId();
                    Log.d("sedAct", "onChanged do islogged: " + userId);//Log.d("frag", "onChanged: chama atividades recentes");

                    userViewModel.loadActivitiesByType(userId, sportType).observe(SportListActivity.this,
                            new Observer<List<PhysicalActivities>>() {
                                @Override
                                public void onChanged(List<PhysicalActivities> physicalActivities) {
                                    //Log.d("frag", "onChanged: setfrag no viewmodel");
                                    physicalActivities = userWithActivities.getPhysicalActivities();
                                    activityAdapter.setActivities(physicalActivities);
                                    activityAdapter.notifyDataSetChanged();
                                }
                            });



                    //Log.d("frag", "onChanged: setadapter");
                    recyclerActivities.setAdapter(activityAdapter);
                    //Log.d("frag", "onChanged: setLayoutManager");
/*                    recyclerActivities.setLayoutManager(
                            new LinearLayoutManager(MainActivity.this,
                                    LinearLayoutManager.VERTICAL,
                                    false));

 */
                } else{
                    startActivity(new Intent(SportListActivity.this,
                            UserLoginActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return  true;
    }
}