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

import java.util.Calendar;
import java.util.Date;

import br.edu.ifsp.dmo.ifitness.model.PhysicalActivities;
import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class SportEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private String title;
    private Button btnDatePicker;
    private TextInputEditText txtHour;
    private TextInputEditText txtMinute;
    private TextInputEditText txtDistance;
    private Button btnSave;
    private Button btnDelete;

    private String userId;
    private String activityKind;
    private double oldDistance;

    private UserViewModel userViewModel;

    private UserWithActivities userWithActivities;
    private PhysicalActivities physicalActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_edit);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        physicalActivities = (PhysicalActivities) intent.getSerializableExtra("activity");

        userViewModel.loadActivitiesById(physicalActivities.getUser(), physicalActivities.getId())
                .observe(this, new Observer<PhysicalActivities>() {
                    @Override
                    public void onChanged(PhysicalActivities physicalActivities) {
                        userViewModel.islogged().observe(SportEditActivity.this, new Observer<UserWithActivities>() {
                            @Override
                            public void onChanged(UserWithActivities userWithActivities) {
                                if (userWithActivities != null) {
                                    SportEditActivity.this.userWithActivities = userWithActivities;
                                    userId = SportEditActivity.this.userWithActivities.getUser().getId();
                                }
                            }
                        });

                        SportEditActivity.this.physicalActivities = physicalActivities;

                        toolbarTitle = findViewById(R.id.toolbar_title);
                        title = SportEditActivity.this.physicalActivities.getActivityCategory();
                        toolbarTitle.setText(title);

                        activityKind = SportEditActivity.this.physicalActivities.getActivityKind();

                        btnDatePicker = findViewById(R.id.sport_edit_btn_date_picker);
                        btnDatePicker.setText(SportEditActivity.this.physicalActivities.getActivityDate());
                        txtHour = findViewById(R.id.sport_edit_edit_txt_hour);
                        txtHour.setText(SportEditActivity.this.physicalActivities.getHours());
                        txtMinute = findViewById(R.id.sport_edit_edit_txt_minute);
                        txtMinute.setText(SportEditActivity.this.physicalActivities.getMinutes());
                        txtDistance = findViewById(R.id.sport_edit_edit_txt_distance);
                        txtDistance.setText(SportEditActivity.this.physicalActivities.getDistance());
                        oldDistance = Double.parseDouble(SportEditActivity.this.physicalActivities.getDistance());

                        btnSave = findViewById(R.id.sport_edit_btn_save);
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updatePhysicalActivity();
                            }
                        });

                        btnDelete = findViewById(R.id.sport_edit_btn_delete);
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deletePhysicalActivity();
                            }
                        });

                        btnDatePicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDatePickerDialog();
                            }
                        });

                    }
                });
    }

    private void deletePhysicalActivity() {
        Double points = Double.parseDouble(SportEditActivity.this.userWithActivities.getUser().getPoints()) - oldDistance;

        userWithActivities.getUser().setPoints(String.valueOf(points));

        userViewModel.deletePhysicalActivity(userWithActivities);
        Toast.makeText(this, getString(R.string.sport_edit_delete), Toast.LENGTH_SHORT).show();

        finish();
    }

    private void updatePhysicalActivity() {
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
        physicalActivities.setId(this.physicalActivities.getId());

        int position = 0;

        for (int i = 0; i < userWithActivities.getPhysicalActivities().size(); i++) {
            if (userWithActivities.getPhysicalActivities().get(i).equals(physicalActivities.getId())) {
                position = i;
            }
        }
        ;

        userWithActivities.getPhysicalActivities().add(position, physicalActivities);

        Double points = Double.parseDouble(userWithActivities.getUser().getPoints())
                - oldDistance
                + Double.parseDouble(txtDistance.getText().toString());

        userWithActivities.getUser().setPoints(String.valueOf(points));

        userViewModel.updatePhysicalActivity(userWithActivities, physicalActivities);
        Toast.makeText(this, getString(R.string.sport_edit_success), Toast.LENGTH_SHORT).show();

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
        month += 1;
        String txtDate = null;
        if (getString(R.string.language).equals("english")){
            txtDate = month + "/" + dayOfMonth + "/" + year;
        }
        if (getString(R.string.language).equals("portuguese")){
            txtDate = dayOfMonth + "/" + month + "/" + year;
        }
        btnDatePicker.setText(txtDate);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}