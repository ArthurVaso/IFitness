package br.edu.ifsp.dmo.ifitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.dmo.ifitness.model.User;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class UserLoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;


    private TextInputEditText txtEmail;
    private TextInputEditText txtPassword;
    private TextView txtForgotPassword;
    private AlertDialog dialogResetPassword;

    private Button btnLogin;
    private Button btnRegister;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.user_login_title));

        txtEmail = findViewById(R.id.user_login_edit_email);
        txtPassword = findViewById(R.id.user_login_edit_password);
        txtForgotPassword = findViewById(R.id.user_login_txt_forgot_password);
        btnLogin = findViewById(R.id.user_login_btn_login);
        btnRegister = findViewById(R.id.user_login_btn_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.login(txtEmail.getText().toString(),
                        txtPassword.getText().toString())
                        .observe(UserLoginActivity.this, new Observer<User>() {
                            @Override
                            public void onChanged(User user) {
                                if (user == null) {
                                    Toast.makeText(getApplicationContext(),
                                            R.string.user_login_wrong_values,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    finish();
                                }
                            }
                        });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this,
                        UserRegisterActivity.class);
                startActivity(intent);
            }
        });

        buildResetPasswordDialog();

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogResetPassword.show();
            }
        });
    }

    private void buildResetPasswordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_reset_password, null);
        TextInputEditText editEmail = view.findViewById(R.id.dialog_reset_edit_email);

        dialogResetPassword = new MaterialAlertDialogBuilder(this)
                .setPositiveButton(R.string.btn_ok, (dialog, which) -> {
                    userViewModel.resetPassword(editEmail.getText().toString());
                    Toast.makeText(UserLoginActivity.this,
                            R.string.user_login_check_email,
                            Toast.LENGTH_SHORT).show();
                    editEmail.getText().clear();
                })
                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> {
                    editEmail.getText().clear();
                })
                .setIcon(android.R.drawable.ic_dialog_email)
                .setView(view)
                .setTitle(R.string.user_login_reset_password)
                .create();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}