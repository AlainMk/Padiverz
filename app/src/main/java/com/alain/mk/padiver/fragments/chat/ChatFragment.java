package com.alain.mk.padiver.fragments.chat;


import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ConvHelper;
import com.alain.mk.padiver.base.BaseFragment;
import butterknife.BindView;

public class ChatFragment extends BaseFragment {

    @BindView(R.id.fragment_chat_recycler_view) RecyclerView recyclerView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void updateData() {

    }


}
