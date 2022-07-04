package br.edu.ifsp.dmo.ifitness;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import br.edu.ifsp.dmo.ifitness.model.User;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class UserRegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextInputEditText txtName;
    private TextInputEditText txtEmail;
    private TextInputEditText txtPassword;
    private TextInputEditText txtConfirmPassword;
    private Button btnRegister;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.user_register_title));

        txtName = findViewById(R.id.user_register_edit_name);
        txtEmail = findViewById(R.id.user_register_edit_email);
        txtPassword = findViewById(R.id.user_register_edit_password);
        txtConfirmPassword = findViewById(R.id.user_register_edit_confirm_password);

        btnRegister = findViewById(R.id.user_register_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                        Toast.makeText(UserRegisterActivity.this,
                                R.string.user_registry_check_password,
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        User user = new User(
                                txtName.getText().toString(),
                                "",
                                "",
                                "",
                                "",
                                txtEmail.getText().toString(),
                                txtPassword.getText().toString(),
                                "0",
                                "",
                                ""
                        );
                        if (user.getPassword().length() >= 6) {
                            // insere um novo usuário no BD
                            userViewModel.createUser(user);
                            // efetua o login do novo usuário
                            userViewModel.login(user.getEmail(), user.getPassword())
                                    .observe(UserRegisterActivity.this, new Observer<User>() {
                                        @Override
                                        public void onChanged(User user) {
                                            finish();
                                        }
                                    });
                        } else {
                            Toast.makeText(UserRegisterActivity.this,
                                    R.string.user_register_error_msg_password,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (txtName.getText().toString().trim().isEmpty()) {
            txtName.setError(getString(R.string.user_register_name_empty));
            isValid = false;
        } else {
            txtName.setError(null);
        }
        if (txtEmail.getText().toString().trim().isEmpty()) {
            txtEmail.setError(getString(R.string.user_register_email_empty));
            isValid = false;
        } else {
            txtEmail.setError(null);
        }
        if (txtPassword.getText().toString().trim().isEmpty()) {
            txtPassword.setError(getString(R.string.user_register_password_empty));
            isValid = false;
        } else {
            txtPassword.setError(null);
        }
        if (txtConfirmPassword.getText().toString().trim().isEmpty()) {
            txtConfirmPassword.setError(getString(R.string.user_register_name_empty));
            isValid = false;
        } else {
            txtConfirmPassword.setError(null);
        }
        return isValid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}