package com.alain.mk.padiver.post;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.home.HomeActivity;

public class PostActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_post;
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
                startActivity(new Intent(PostActivity.this, HomeActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
