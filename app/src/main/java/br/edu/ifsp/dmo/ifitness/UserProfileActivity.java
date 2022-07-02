package br.edu.ifsp.dmo.ifitness;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final int REQUEST_TAKE_PHOTO = 1;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextInputEditText txtName;
    private TextInputEditText txtSurname;
    private TextInputEditText txtEmail;
    private Button btnDatePicker;
    private TextInputEditText txtPhone;
    private Spinner spnGender;
    private ImageView profileImage;
    private Uri photoURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.user_profile_title));

        txtName = findViewById(R.id.user_profile_txt_name);
        txtSurname = findViewById(R.id.user_profile_txt_surname);
        txtEmail = findViewById(R.id.user_profile_txt_email);
        btnDatePicker = findViewById(R.id.user_profile_btn_date_picker);
        txtPhone = findViewById(R.id.user_profile_txt_phone);
        spnGender = findViewById(R.id.user_profile_sp_gender);
        profileImage = findViewById(R.id.user_profile_image);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        String imageProfile = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(MediaStore.EXTRA_OUTPUT, null);

        if(imageProfile != null){
            photoURI = Uri.parse(imageProfile);
            profileImage.setImageURI(photoURI);
        }else{
            profileImage.setImageResource(R.drawable.profile_image);
        }
    }

    public void showDatePickerDialog(){
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
        String txtDate = dayOfMonth + "/" + month + "/" + year;
        btnDatePicker.setText(txtDate);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            photoURI = FileProvider.getUriForFile(this,
                    "br.edu.ifsp.dmo.ifitness.fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile("PROFILE_" + timestamp,".jpg", storageDir);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}