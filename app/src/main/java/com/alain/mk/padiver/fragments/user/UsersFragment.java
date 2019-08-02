package com.alain.mk.padiver.fragments.user;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.message.MessageActivity;
import com.alain.mk.padiver.models.User;
import com.alain.mk.padiver.utils.ItemClickSupport;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import butterknife.BindView;

public class UsersFragment extends BaseFragment {

    @BindView(R.id.fragment_users_recycler_view) RecyclerView recyclerView;

    private UserAdapter userAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_users;
    }

    @Override
    protected void updateData() {

        this.configureRecyclerview();
    }
    // --------------------
    // REST REQUESTS
    // --------------------

    private void configureRecyclerview() {

        this.userAdapter = new UserAdapter(generateOptionsForAdapter(UserHelper.getUsersCollection()), Glide.with(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.userAdapter);

        ItemClickSupport.addTo(recyclerView, R.layout.fragment_users_item)
                .setOnItemClickListener((rv, position, v) -> this.startMessageActivity(this.userAdapter.getUser(position)));
    }

    private void showBottomSheet(User user) {
        UsersModalFragment fragment = new UsersModalFragment();
        fragment.setUser(user);
        fragment.show(getActivity().getSupportFragmentManager(), "MODAL");
    }

    private void startMessageActivity(User user) {

        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_ID, user.getUid());
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_USERNAME, user.getUsername());
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_IMAGE_URL, user.getUrlPicture());
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
}
