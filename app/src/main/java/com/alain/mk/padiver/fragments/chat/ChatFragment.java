package com.alain.mk.padiver.fragments.chat;


import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ConvHelper;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.message.MessageActivity;
import com.alain.mk.padiver.models.Message;
import com.alain.mk.padiver.utils.ItemClickSupport;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import butterknife.BindView;

public class ChatFragment extends BaseFragment {

    @BindView(R.id.fragment_chat_toolbar) Toolbar toolbar;
    @BindView(R.id.fragment_chat_recycler_view) RecyclerView recyclerView;

    private ChatAdapter chatAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void updateData() {

        this.configureToolbar();
        this.configureRecyclerView(this.getCurrentUser().getUid());
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

    private void configureRecyclerView(String senderId) {

        this.chatAdapter = new ChatAdapter(generateOptionsForAdapter(ConvHelper.getAllConversation(senderId)), Glide.with(this));

        ItemClickSupport.addTo(recyclerView, R.layout.fragment_chat_item)
                .setOnItemClickListener((rv, position, v) -> this.startMessageActivity(this.chatAdapter.getConversation(position)));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.chatAdapter);
    }

    // --------------------
    // UI
    // --------------------
    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){

        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }

    private void startMessageActivity(Message message) {

        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_ID, message.getUserSender().getUid());
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_USERNAME, message.getUserSender().getUsername());
        intent.putExtra(MessageActivity.BUNDLE_KEY_PROJECT_IMAGE_URL, message.getUserSender().getUrlPicture());
        startActivity(intent);
    }
}
