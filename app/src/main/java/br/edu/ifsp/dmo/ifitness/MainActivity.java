package br.edu.ifsp.dmo.ifitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private DrawerLayout toolbarDrawer;

    private LinearLayout layoutIcomWalk;
    private LinearLayout layoutIcomRun;
    private LinearLayout layoutIcomSwim;
    private LinearLayout layoutIcomBike;

    private NavigationView navigationView;
    private TextView txtLogin;


    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.app_name));

        toolbarDrawer = findViewById(R.id.nav_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                toolbarDrawer, toolbar,  R.string.toggle_open, R.string.toggle_close);
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
                        Toast.makeText(MainActivity.this, "Sair",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_test:
                        intent = new Intent(MainActivity.this,
                                SportEditActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
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
        //lista das ultimas atividades


    }
/* aqui é o sistema para colocar a foto do usuario
    @Override
    protected void onResume() {
        super.onResume();
        usuarioViewModel.isLogged().observe(this, new Observer<UsuarioComEndereco>() {
            @Override
            public void onChanged(UsuarioComEndereco usuarioComEndereco) {
                if(usuarioComEndereco != null){
                    txtLogin.setText(usuarioComEndereco.getUsuario().getNome()
                            + " " + usuarioComEndereco.getUsuario().getSobrenome());
                    String perfilImage = PreferenceManager
                            .getDefaultSharedPreferences(MainActivity.this)
                            .getString(MediaStore.EXTRA_OUTPUT, null);
                    if(perfilImage != null){
                        profileImage.setImageURI(Uri.parse(perfilImage));
                    }else{
                        profileImage.setImageResource(R.drawable.profile_image);
                    }
                }
            }
        });
    }
*/
    @Override
    public void onBackPressed() {
        if(toolbarDrawer.isDrawerOpen(GravityCompat.START)){
            toolbarDrawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}