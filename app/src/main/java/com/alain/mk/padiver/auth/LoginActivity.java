package com.alain.mk.padiver.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_edit_email) EditText editEmail;
    @BindView(R.id.activity_login_edit_password) EditText editPassword;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.activity_login_text_sign_up)
    public void onClickTextSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @OnClick(R.id.activity_login_button_login)
    public void onClickLoginButton() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Invalid mail");
            editEmail.setFocusable(true);
        } else if (password.length() < 6) {
            editPassword.setError("Password lenght at least 6 characters");
            editPassword.setFocusable(true);
        } else {
            this.loginInFirebase(email, password);
        }

    }

    private void loginInFirebase(String email, String password) {

        progressDialog.setMessage("Login...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            editEmail.setError("" + e.getMessage());
            editPassword.setError("" + e.getMessage());
        });

    }
}
