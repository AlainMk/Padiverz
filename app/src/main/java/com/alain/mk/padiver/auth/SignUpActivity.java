package com.alain.mk.padiver.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.home.HomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.activity_sign_up_edit_email) EditText editEmail;
    @BindView(R.id.activity_sign_up_edit_username) EditText editUsername;
    @BindView(R.id.activity_sign_up_edit_password) EditText editPassword;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        this.configureToolbar();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_sign_up;
    }

    @OnClick(R.id.activity_sign_up_text_login)
    public void onClickTextLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.activity_sign_up_button_sign)
    public void onClickSignUpButton() {

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String username = editUsername.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Invalid mail");
            editEmail.setFocusable(true);
        } else if (password.length() < 6) {
            editPassword.setError("Password lenght at least 6 characters");
            editPassword.setFocusable(true);
        }else if (username.isEmpty()) {

            editUsername.setError("Ce champ n'est peut être vide");
            editUsername.setFocusable(true);
        }else {
            this.signUpWithEmailAndPassword(email, password);
        }
    }

    // --------------------
    // REST REQUEST
    // --------------------

    // Http request that create user in firestore
    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = editUsername.getText().toString();
            String email = this.getCurrentUser().getEmail();
            String uid = this.getCurrentUser().getUid();

            String deviceToken = FirebaseInstanceId.getInstance().getToken();

            this.getCurrentUser().getIdToken(true).addOnSuccessListener(getTokenResult -> {
                String tokenId = getTokenResult.getToken();
                UserHelper.createUser(uid, username, email, urlPicture, tokenId, deviceToken).addOnFailureListener(this.onFailureListener());
            });

        }
    }

    private void signUpWithEmailAndPassword(String email, String password) {

        progressDialog.setMessage("Sign Up...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                this.createUserInFirestore();
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
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
