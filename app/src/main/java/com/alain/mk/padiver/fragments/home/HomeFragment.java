package com.alain.mk.padiver.fragments.home;


import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ArticleHelper;
import com.alain.mk.padiver.api.PostHelper;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.detail.DetailPostActivity;
import com.alain.mk.padiver.fragments.comment.CommentsModalFragment;
import com.alain.mk.padiver.models.Post;
import com.alain.mk.padiver.post.PostActivity;
import com.alain.mk.padiver.utils.ItemClickSupport;
import com.alain.mk.padiver.utils.ViewAnimation;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements HomeAdapter.Listener{

    @BindView(R.id.fragment_home_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fragment_home_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fragment_home_floating_actiion_button) FloatingActionButton floatingActionButton;
    @BindView(R.id.fragment_home_nested_scroll_view) NestedScrollView nestedScrollView;

    private HomeAdapter homeAdapter;
    private static final int REGISTER_LIKE = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void updateData() {

        this.configureRecyclerView();
        this.iniComponent();
    }

    @OnClick(R.id.fragment_home_floating_actiion_button)
    public void onClickFloatingActionButton() {
        startActivity(new Intent(getActivity(), PostActivity.class));
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

    // --------------------
    // REST REQUESTS
    // --------------------
    private void configureRecyclerView() {

        this.homeAdapter = new HomeAdapter(generateOptionsForAdapter(PostHelper.getPostCollection()), Glide.with(this), this.getCurrentUser().getUid(), this);

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

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return aVoid -> {
            switch (origin) {
                case REGISTER_LIKE:
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.register_like, Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                    break;
                default:
                    break;
            }
        };
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

    }

    @Override
    public void onClickLikeButton(int position) {

        Post post = homeAdapter.getPost(position);

        ArticleHelper.getLikeReference(post.getTitle()).document(this.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (!task.getResult().exists()){

                ArticleHelper.createLike(this.getCurrentUser().getUid(),post.getTitle()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(REGISTER_LIKE))
                .addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }else {
                ArticleHelper.deleteLike(this.getCurrentUser().getUid(), post.getTitle()).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onClickCommentButton(int position) {
        Post post = homeAdapter.getPost(position);
        CommentsModalFragment fragment = new CommentsModalFragment();
        fragment.setPost(post);
        fragment.show(getActivity().getSupportFragmentManager(), "MODAL");
    }
}
