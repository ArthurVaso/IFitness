package br.edu.ifsp.dmo.ifitness;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.edu.ifsp.dmo.ifitness.repository.DatePickerFragment;

public class DetailSportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private String title;
    private Button btndatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sport);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        title = intent.getStringExtra("title").toString();

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);

        btndatePicker = findViewById(R.id.detail_sport_btn_date_picker);
        btndatePicker.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showDatePickerDialog(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
        Log.d("picker", "showDatePickerDialog: " + fragment.getTag() + " | " +
                fragment.toString());
        btndatePicker.setText(fragment.toString());
    }
}