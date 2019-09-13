package com.alain.mk.padiver.fragments.profile;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ArticleHelper;
import com.alain.mk.padiver.api.PostHelper;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.auth.ActivityEditProfile;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.detail.DetailPostActivity;
import com.alain.mk.padiver.fragments.comment.CommentsModalFragment;
import com.alain.mk.padiver.fragments.home.HomeAdapter;
import com.alain.mk.padiver.models.Post;
import com.alain.mk.padiver.models.User;
import com.alain.mk.padiver.utils.ItemClickSupport;
import com.alain.mk.padiver.utils.NetworkCheck;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment implements HomeAdapter.Listener{

    @BindView(R.id.fragment_profile_toolbar) Toolbar toolbar;
    @BindView(R.id.fragment_profile_text_username) TextView textViewUsername;
    @BindView(R.id.fragment_profile_text_about) TextView textViewAbout;
    @BindView(R.id.fragment_profile_text_hobbies) TextView textViewHobbies;
    @BindView(R.id.fragment_profile_text_location) TextView textViewLocation;
    @BindView(R.id.fragment_profile_text_skills) TextView textViewSkills;
    @BindView(R.id.fragment_profile_text_git_hub_link) TextView textViewGithubLink;
    @BindView(R.id.fragment_profile_text_web_site) TextView textViewWebSite;
    @BindView(R.id.fragment_profile_text_phone_number) TextView textViewPhoneNumber;
    @BindView(R.id.fragment_profile_image_profile) ImageView imageViewProfile;
    @BindView(R.id.fragment_profile_progress_bar) ProgressBar progressBar;
    @BindView(R.id.fragment_profile_recycler_view) RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private HomeAdapter homeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void updateData() {

        this.configureToolbar();
        this.updateUIWhenCreating();
        this.configureRecyclerView();
    }

    @OnClick(R.id.fragment_profile_floating_action_button)
    public void onClickFloatingActionButton() {
        startActivity(new Intent(getActivity(), ActivityEditProfile.class));
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

    private void updateUIWhenCreating() {

        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {

            User modelCurrentUser = documentSnapshot.toObject(User.class);

            if (modelCurrentUser.getUrlPicture() != null) {
                Glide.with(this)
                        .load(modelCurrentUser.getUrlPicture())
                        .into(imageViewProfile);
            }

            String phoneNumber = TextUtils.isEmpty(modelCurrentUser.getPhoneNumber()) ? getString(R.string.info_no_phone_number) : modelCurrentUser.getPhoneNumber();
            this.textViewPhoneNumber.setText(phoneNumber);

            String email = TextUtils.isEmpty(modelCurrentUser.getHobbies()) ? getString(R.string.info_no_hobbies_found) : modelCurrentUser.getHobbies();
            this.textViewHobbies.setText(email);

            String address = TextUtils.isEmpty(modelCurrentUser.getAddress()) ? getString(R.string.info_no_address_location) : modelCurrentUser.getAddress();
            this.textViewLocation.setText(address);

            String githubLink = TextUtils.isEmpty(modelCurrentUser.getGithubLink()) ? getString(R.string.info_no_github_link) : modelCurrentUser.getGithubLink();
            this.textViewGithubLink.setText(githubLink);

            String bio = TextUtils.isEmpty(modelCurrentUser.getBio()) ? getString(R.string.info_no_bio) : modelCurrentUser.getBio();
            this.textViewAbout.setText(bio);

            String webSite = TextUtils.isEmpty(modelCurrentUser.getWebSite()) ? getString(R.string.info_no_web_site) : modelCurrentUser.getWebSite();
            this.textViewWebSite.setText(webSite);

            String language = TextUtils.isEmpty(modelCurrentUser.getLanguage()) ? getString(R.string.info_no_language) : modelCurrentUser.getLanguage();
            this.textViewSkills.setText(language);

            String username = TextUtils.isEmpty(modelCurrentUser.getUsername()) ? getString(R.string.info_no_username_found) : modelCurrentUser.getUsername();
            this.textViewUsername.setText(username);
        });

    }

    private void configureRecyclerView() {

        this.homeAdapter = new HomeAdapter(generateOptionsForAdapter(PostHelper.getPostCollectionOrderByUser(this.getCurrentUser().getUid())), Glide.with(this), this.getCurrentUser().getUid(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.homeAdapter);

        ItemClickSupport.addTo(recyclerView, R.layout.fragment_home_post_item)
                .setOnItemClickListener((rv, position, v) -> this.startMessageActivity(this.homeAdapter.getPost(position)));
    }

    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    private FirestoreRecyclerOptions<Post> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void startMessageActivity(Post post) {

        Intent intent = new Intent(getActivity(), DetailPostActivity.class);
        intent.putExtra(DetailPostActivity.BUNDLE_KEY_PROJECT_USERNAME, post.getUserSender().getUsername());
        intent.putExtra(DetailPostActivity.BUNDLE_KEY_PROJECT_IMAGE_PROFILE, post.getUserSender().getUrlPicture());
        intent.putExtra(DetailPostActivity.BUNDLE_KEY_PROJECT_TITLE, post.getTitle());
        intent.putExtra(DetailPostActivity.BUNDLE_KEY_PROJECT_IMAGE_POST, post.getUrlImage());
        intent.putExtra(DetailPostActivity.BUNDLE_KEY_PROJECT_DESCRIPTION, post.getDescription());
        intent.putExtra(DetailPostActivity.BUNDLE_KEY_PROJECT_TAGS, post.getTags());
        intent.putExtra(DetailPostActivity.BUNDLE_KEY_PROJECT_DATE, post.getDateCreated().toString());
        startActivity(intent);
    }

    @Override
    public void onDataChanged() {

        // Show ProgressBar in case RecyclerView is empty
        progressBar.setVisibility(this.homeAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClickLikeButton(int position) {

        Post post = homeAdapter.getPost(position);

        if (NetworkCheck.isConnect(getActivity())) {
            ArticleHelper.getLikeReference(post.getTitle()).document(this.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                if (!task.getResult().exists()){

                    ArticleHelper.createLike(this.getCurrentUser().getUid(),post.getTitle()).addOnFailureListener(e ->
                                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());
                }else {
                    ArticleHelper.deleteLike(this.getCurrentUser().getUid(), post.getTitle()).addOnFailureListener(e ->
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickCommentButton(int position) {
        Post post = homeAdapter.getPost(position);
        CommentsModalFragment fragment = new CommentsModalFragment();
        fragment.setPost(post);
        fragment.show(getActivity().getSupportFragmentManager(), "MODAL");
    }
}
