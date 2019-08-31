package com.alain.mk.padiver.detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class DetailPostActivity extends BaseActivity {

    @BindView(R.id.activity_detail_post_user_name) TextView textUsername;
    @BindView(R.id.activity_detail_post_text_title) TextView textTitle;
    @BindView(R.id.activity_detail_post_text_tags) TextView textTags;
    @BindView(R.id.activity_detail_post_text_date) TextView textDate;
    @BindView(R.id.activity_detail_post_text_description) TextView textDescription;
    @BindView(R.id.activity_detail_post_text_likes_count) TextView textLikesCount;
    @BindView(R.id.activity_detail_post_image_profile) ImageView imageViewProfile;
    @BindView(R.id.activity_detail_post_image_post) ImageView imageViewPost;
    @BindView(R.id.activity_detail_post_button_comment) ImageButton buttonComment;
    @BindView(R.id.activity_detail_post_nested_scroll_view) NestedScrollView nestedScrollView;
    @BindView(R.id.activity_detail_post_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_detail_post_floating_action_button) FloatingActionButton floatingActionButton;

    public static final String BUNDLE_KEY_PROJECT_USERNAME = "username";
    public static final String BUNDLE_KEY_PROJECT_IMAGE_PROFILE = "urlPicture";
    public static final String BUNDLE_KEY_PROJECT_TITLE = "title";
    public static final String BUNDLE_KEY_PROJECT_IMAGE_POST = "urlImage";
    public static final String BUNDLE_KEY_PROJECT_DESCRIPTION = "description";
    public static final String BUNDLE_KEY_PROJECT_TAGS = "tags";
    public static final String BUNDLE_KEY_PROJECT_DATE = "dateCreated";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateUIWhenCreating();
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

    private void updateUIWhenCreating() {

        String name = getIntent().getStringExtra("username");
        String imageProfile = getIntent().getStringExtra("urlPicture");
        String title = getIntent().getStringExtra("title");
        String tags = getIntent().getStringExtra("tags");
        String description = getIntent().getStringExtra("description");
        String imagePost = getIntent().getStringExtra("urlImage");
        String dateCreated = getIntent().getStringExtra("dateCreated");

        if (imageProfile != null) {
            Glide.with(this)
                    .load(imageProfile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);
        }

        if (imagePost != null){
            Glide.with(this).load(imagePost)
                    .into(imageViewPost);
            this.imageViewPost.setVisibility(View.VISIBLE);
        } else {
            this.imageViewPost.setVisibility(View.GONE);
        }

        this.textUsername.setText(name);
        this.textTitle.setText(title);
        this.textTags.setText(tags);
        this.textDescription.setText(description);
        this.textDate.setText(dateCreated);
    }

}
