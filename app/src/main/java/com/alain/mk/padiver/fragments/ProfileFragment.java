package com.alain.mk.padiver.fragments;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alain.mk.padiver.ImageActivity;
import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.auth.LoginActivity;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void updateData() {

    }

    @OnClick(R.id.fragment_container_image_sent_cardview_image)
    public void onClickImageProfile() {

        this.startMessageActivity();
    }

    private void startMessageActivity() {

        Intent intent = new Intent(getActivity(), ImageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
