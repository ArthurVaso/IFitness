package br.edu.ifsp.dmo.ifitness;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import br.edu.ifsp.dmo.ifitness.model.PhysicalActivities;
import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class SportRegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private String title;
    private Button btnDatePicker;
    private TextInputEditText txtHour;
    private TextInputEditText txtMinute;
    private TextInputEditText txtDistance;
    private Button btnSave;

    private String userId;
    private String activityKind;

    private UserViewModel userViewModel;

    private UserWithActivities userWithActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_register);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        title = intent.getStringExtra("title").toString();

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);

        if (title.equalsIgnoreCase(getString(R.string.sports_walking))) {
            activityKind = getString(R.string.kind_walking);
        }
        if (title.equals(getString(R.string.sports_running))) {
            activityKind = getString(R.string.kind_running);
        }
        if (title.equals(getString(R.string.sports_swimming))) {
            activityKind = getString(R.string.kind_swimming);
        }
        if (title.equals(getString(R.string.sports_cycling))) {
            activityKind = getString(R.string.kind_cycling);
        }

        btnDatePicker = findViewById(R.id.sport_register_btn_date_picker);
        btnDatePicker.setText(new SimpleDateFormat(getString(R.string.sport_register_date_pattern)).format(new Date()));
        txtHour = findViewById(R.id.sport_register_edit_txt_hour);
        txtMinute = findViewById(R.id.sport_register_edit_txt_minute);
        txtDistance = findViewById(R.id.sport_register_edit_txt_distance);

        btnSave = findViewById(R.id.sport_register_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhysicalActivity();
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        userViewModel.islogged().observe(this, new Observer<UserWithActivities>() {
            @Override
            public void onChanged(UserWithActivities userWithActivities) {
                if (userWithActivities != null) {
                    userId = userWithActivities.getUser().getId();
                    SportRegisterActivity.this.userWithActivities = userWithActivities;
                }
            }
        });
    }

    private void savePhysicalActivity() {
        if (!validate()) {
            return;
        }

        String timestamp = String.valueOf(new Date().getTime());

        PhysicalActivities physicalActivities = new PhysicalActivities(
                userId,
                title,
                txtDistance.getText().toString(),
                txtHour.getText().toString(),
                txtMinute.getText().toString(),
                btnDatePicker.getText().toString(),
                timestamp,
                activityKind);

        userWithActivities.getPhysicalActivities().add(0, physicalActivities);

        Double points = Double.parseDouble(userWithActivities.getUser().getPoints())
                + Double.parseDouble(txtDistance.getText().toString());

        userWithActivities.getUser().setPoints(String.valueOf(points));

        userViewModel.addActivity(userWithActivities);
        Toast.makeText(this, getString(R.string.sport_edit_success), Toast.LENGTH_LONG).show();

        finish();
    }

    private boolean validate() {
        boolean isValid = true;
        if (txtHour.getText().toString().trim().isEmpty()) {
            txtHour.setError("Fill the field name.");
            isValid = false;
        } else {
            txtHour.setError(null);
        }
        if (txtMinute.getText().toString().trim().isEmpty()) {
            txtMinute.setError("Fill the field surname.");
            isValid = false;
        } else {
            txtMinute.setError(null);
        }
        if (txtDistance.getText().toString().trim().isEmpty()) {
            txtDistance.setError("Fill the field  e-mail.");
            isValid = false;
        } else {
            txtDistance.setError(null);
        }
        return isValid;
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year;
        btnDatePicker.setText(date);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}