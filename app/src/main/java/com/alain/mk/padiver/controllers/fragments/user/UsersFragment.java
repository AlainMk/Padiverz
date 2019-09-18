package com.alain.mk.padiver.controllers.fragments.user;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.adapter.UserAdapter;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.controllers.activities.message.MessageActivity;
import com.alain.mk.padiver.models.User;
import com.alain.mk.padiver.controllers.activities.profile.ProfileActivity;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import butterknife.BindView;

public class UsersFragment extends BaseFragment implements UserAdapter.Listener {

    @BindView(R.id.fragment_users_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fragment_users_toolbar) Toolbar toolbar;
    @BindView(R.id.fragment_users_shimmer_container) ShimmerFrameLayout container;

    private UserAdapter userAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_users;
    }

    @Override
    protected void updateData() {

        this.configureToolbar();
        this.configureRecyclerview();
    }

    @Override
    public void onResume() {
        super.onResume();
        container.startShimmer();
    }

    @Override
    public void onStop() {
        super.onStop();
        container.stopShimmer();
    }


    private void configureToolbar() {
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_logout:
                    return true;
                case R.id.menu_search:
                    return true;
            }
            return super.onOptionsItemSelected(item);
        });

    }

    // --------------------
    // REST REQUESTS
    // --------------------

    private void configureRecyclerview() {

        this.userAdapter = new UserAdapter(generateOptionsForAdapter(UserHelper.getUsersCollection()), Glide.with(this), this);

        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(this.userAdapter);
    }

    private void startMessageActivity(User user) {

        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_ID, user.getUid());
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_USERNAME, user.getUsername());
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_IMAGE_URL, user.getUrlPicture());
        startActivity(intent);
    }

    private void startProfileActivity(User user) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.BUNDLE_KEY_PROJECT_ID, user.getUid());
        startActivity(intent);
    }

    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onDataChanged() {

        container.setVisibility(this.userAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClickMessageButton(int position) {
        this.startMessageActivity(this.userAdapter.getUser(position));
    }

    @Override
    public void onClickProfileButton(int position) {
        this.startProfileActivity(this.userAdapter.getUser(position));
    }
}
