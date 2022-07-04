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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.edu.ifsp.dmo.ifitness.model.User;
import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

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
    
    private Button btnUpdate;
    
    private Uri photoURI;
    
    private UserViewModel userViewModel;

    private UserWithActivities userWithActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

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
        btnUpdate = findViewById(R.id.user_profile_btn_save);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

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

        userViewModel.islogged().observe(this, new Observer<UserWithActivities>() {
            @Override
            public void onChanged(UserWithActivities userWithActivities) {
                if(userWithActivities != null){

                    UserProfileActivity.this.userWithActivities = userWithActivities;
                    txtName.setText(userWithActivities.getUser().getName());
                    txtSurname.setText(userWithActivities.getUser().getSurname());
                    txtEmail.setText(userWithActivities.getUser().getEmail());
                    btnDatePicker.setText(userWithActivities.getUser().getBirthdayDate());
                    txtPhone.setText(userWithActivities.getUser().getPhone());

                    String[] gender = getResources().getStringArray(R.array.gender);
                    for (int i = 0; i < gender.length; i++){
                        if(gender[i].equals(userWithActivities.getUser().getGender())){
                            spnGender.setSelection(i);
                        }
                    }
                }else{
                    startActivity(new Intent(UserProfileActivity.this,
                            UserLoginActivity.class));
                    finish();
                }
            }
        });
    }

    private void update() {
        if(!validate()){
            return;
        }

        userWithActivities.getUser().setName(txtName.getText().toString());
        userWithActivities.getUser().setSurname(txtSurname.getText().toString());
        userWithActivities.getUser().setEmail(txtEmail.getText().toString());
        userWithActivities.getUser().setBirthdayDate(btnDatePicker.getText().toString());
        userWithActivities.getUser().setPhone(txtPhone.getText().toString());

        getResources().getStringArray(R.array.gender);
        userWithActivities.getUser().setGender(getResources().getStringArray(R.array.gender)[spnGender.getSelectedItemPosition()]);

        userViewModel.update(userWithActivities);
        Toast.makeText(this, getString(R.string.user_profile_success), Toast.LENGTH_SHORT).show();
    }

    private boolean validate(){
        boolean isValid = true;
        if(txtName.getText().toString().trim().isEmpty()){
            txtName.setError("Fill the field name.");
            isValid = false;
        }else{
            txtName.setError(null);
        }
        if(txtSurname.getText().toString().trim().isEmpty()){
            txtSurname.setError("Fill the field surname.");
            isValid = false;
        }else{
            txtSurname.setError(null);
        }
        if(txtEmail.getText().toString().trim().isEmpty()){
            txtEmail.setError("Fill the field  e-mail.");
            isValid = false;
        }else{
            txtEmail.setError(null);
        }
        if(txtPhone.getText().toString().trim().isEmpty()){
            txtPhone.setError("Fill the field  phone.");
            isValid = false;
        }else{
            txtPhone.setError(null);
        }
        return isValid;
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