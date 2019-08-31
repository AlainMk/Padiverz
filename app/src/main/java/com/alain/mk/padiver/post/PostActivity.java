package com.alain.mk.padiver.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.PostHelper;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.home.HomeActivity;
import com.alain.mk.padiver.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PostActivity extends BaseActivity {

    @BindView(R.id.activity_post_edit_text_title) EditText editTitle;
    @BindView(R.id.activity_post_edit_tags) NachoTextView editTags;
    @BindView(R.id.activity_post_edit_text_description) EditText editDescription;
    @BindView(R.id.activity_post_image_view) ImageView editImageView;

    private ProgressDialog progressDialog;
    @Nullable
    private User modelCurrentUser;

    public static final int REGISTER_MEMBER = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        this.configureToolbar();
        this.iniComponent();
        this.getCurrentUserFromFirestore();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_post;
    }

    @OnClick(R.id.activity_post_floating_action_button)
    public void onClickFloatingActionButton() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_check:
                this.createPostInFirestore();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniComponent() {
        List<String> items = new ArrayList<>();
        items.add(editTags.getText().toString());
        editTags.setText(items);
        editTags.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
    }

    private void createPostInFirestore() {

        if (TextUtils.isEmpty(editTitle.getText())) {

            editTitle.setError("Ce champ n'est peut être vide");
            editTitle.setFocusable(true);
        } else if (TextUtils.isEmpty(editTags.getText())) {

            editTags.setError("Ce champ n'est peut être vide");
            editTags.setFocusable(true);
        } else if (TextUtils.isEmpty(editDescription.getText())) {

            editDescription.setError("Ce champ n'est peut être vide");
            editDescription.setFocusable(true);
        } else {

            progressDialog.setMessage("Publication...");
            progressDialog.show();
            List<String> items = new ArrayList<>();
            items.add(editTags.getText().toString());
            editTags.setText(items);
            editTags.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);

            PostHelper.createPost(editTitle.getText().toString(), editTags.getText().toString(), editDescription.getText().toString(), modelCurrentUser)
                    .addOnSuccessListener(updateUIAfterRESTRequestsCompleted(REGISTER_MEMBER))
                    .addOnFailureListener(this.onFailureListener());
        }
    }

    // --------------------
    // REST REQUESTS
    // --------------------
    // Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    // --------------------
    // UI
    // --------------------

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<DocumentReference> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
            switch (origin){
                case REGISTER_MEMBER:
                    progressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this, HomeActivity.class));
                    break;
                default:
                    break;
            }
        };
    }
}
