package com.alain.mk.padiver.detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;

public class ActivityDetailPost extends BaseActivity {

    @BindView(R.id.activity_detail_post_user_name) TextView textUsername;
    @BindView(R.id.activity_detail_post_text_title) TextView textTitle;
    @BindView(R.id.activity_detail_post_text_tags) TextView textTags;
    @BindView(R.id.activity_detail_post_text_description) TextView textDescription;
    @BindView(R.id.activity_detail_post_text_likes_count) TextView textLikesCount;
    @BindView(R.id.activity_detail_post_image_profile) ImageView imageProfile;
    @BindView(R.id.activity_detail_post_image_post) ImageView imagePost;
    @BindView(R.id.activity_detail_post_button_comment) ImageButton buttonComment;
    @BindView(R.id.activity_detail_post_nested_scroll_view) NestedScrollView nestedScrollView;
    @BindView(R.id.activity_detail_post_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_detail_post_floating_action_button) FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initToolbar();
        this.iniComponent();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_detail_post;
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    boolean hide = false;

    private void iniComponent() {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY >= oldScrollY) { // down
                if (hide) return;
                ViewAnimation.hideFab(floatingActionButton);
                hide = true;
            } else {
                if (!hide) return;
                ViewAnimation.showFab(floatingActionButton);
                hide = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_book_mark:
                Toast.makeText(this, "Bientôt cette fonctionnalité sera mise en place", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
