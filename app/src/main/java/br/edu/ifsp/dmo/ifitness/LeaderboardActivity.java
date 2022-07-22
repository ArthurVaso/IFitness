package br.edu.ifsp.dmo.ifitness;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.dmo.ifitness.adapter.ActivityAdapter;
import br.edu.ifsp.dmo.ifitness.adapter.LeaderboardAdapter;
import br.edu.ifsp.dmo.ifitness.model.User;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class LeaderboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    
    private UserViewModel userViewModel;
    private RecyclerView recyclerLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.leaderboard_title);

        recyclerLeaderboard = findViewById(R.id.leaderboard_recycler_activities);

        leaderboardAdapter = new LeaderboardAdapter(LeaderboardActivity.this);

        userViewModel.loadUsers().observe(this,
                new Observer<List<User>>() {
                    @Override
                    public void onChanged(List<User> users) {
                        leaderboardAdapter.setUsers(users);
                        leaderboardAdapter.notifyDataSetChanged();
                    }
                });

        recyclerLeaderboard.setAdapter(leaderboardAdapter);
        recyclerLeaderboard.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}