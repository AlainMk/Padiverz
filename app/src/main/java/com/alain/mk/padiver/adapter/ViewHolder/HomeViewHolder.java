package com.alain.mk.padiver.adapter.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.adapter.HomeAdapter;
import com.alain.mk.padiver.models.Post;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_home_post_item_linear_like) LinearLayout linearLayoutLike;
    @BindView(R.id.fragment_home_post_item_linear_comment) LinearLayout linearLayoutComment;
    @BindView(R.id.fragment_home_post_item_user_name) TextView textUsername;
    @BindView(R.id.fragment_home_post_item_image_profile) ImageView imageProfile;
    @BindView(R.id.fragment_home_post_item_tags) TextView textTags;
    @BindView(R.id.fragment_home_post_item_text_title) TextView textTitle;
    @BindView(R.id.fragment_home_post_item_text_description) TextView textDescription;
    @BindView(R.id.fragment_home_post_item_image_post) ImageView imagePost;
    @BindView(R.id.fragment_home_post_item_button_like) public ImageView buttonLike;
    @BindView(R.id.fragment_home_post_item_button_comment) public ImageView buttonComment;
    @BindView(R.id.fragment_home_post_item_text_count_like) public TextView textCountLikes;
    @BindView(R.id.fragment_home_post_item_text_count_comments) public TextView textCountComments;
    // DATE TEXT
    @BindView(R.id.fragment_home_post_item_text_date) TextView textDate;

    private WeakReference<HomeAdapter.Listener> callbackWeakRef;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithPost(Post post, RequestManager glide, HomeAdapter.Listener callback) {

        textUsername.setText(post.getUserSender().getUsername());
        textTags.setText(post.getTags());
        textTitle.setText(post.getTitle());
        textDescription.setText(post.getDescription());
        // Update date TextView
        if (post.getDateCreated() != null) this.textDate.setText(this.convertDateToHour(post.getDateCreated()));

        if (post.getUserSender().getUrlPicture() != null) {
            glide.load(post.getUserSender().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageProfile);
        }
        // Update image sent ImageView
        if (post.getUrlImage() != null){
            glide.load(post.getUrlImage())
                    .into(imagePost);
            this.imagePost.setVisibility(View.VISIBLE);
        } else {
            this.imagePost.setVisibility(View.GONE);
        }

        this.linearLayoutLike.setOnClickListener(v -> {

            HomeAdapter.Listener callback1 = callbackWeakRef.get();
            if (callback1 != null) callback1.onClickLikeButton(getAdapterPosition());
        });

        this.linearLayoutComment.setOnClickListener(v -> {

            HomeAdapter.Listener callback12 = callbackWeakRef.get();
            if (callback12 != null) callback12.onClickCommentButton(getAdapterPosition());
        });

        this.callbackWeakRef = new WeakReference<>(callback);
    }

    // ---

    private String convertDateToHour(Date date){
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }
}
