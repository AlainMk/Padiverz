package com.alain.mk.padiver.fragments;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.alain.mk.padiver.ImageActivity;
import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.auth.LoginActivity;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.home.HomeActivity;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.fragment_profile_toolbar) Toolbar toolbar;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void updateData() {

        this.configureToolbar();
    }


    private void configureToolbar() {
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_logout:
                    firebaseAuth.signOut();
                    return true;
                case R.id.menu_search:
                    return true;
            }
            return super.onOptionsItemSelected(item);
        });

    }
}
