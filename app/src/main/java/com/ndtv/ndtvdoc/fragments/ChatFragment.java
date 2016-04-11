package com.ndtv.ndtvdoc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.models.ChatModel;
import com.ndtv.ndtvdoc.utils.AppConstants;
import com.ndtv.ndtvdoc.utils.NDTVSharedPreferencesManager;

/**
 * Created by Shivam on 4/11/2016.
 */

public class ChatFragment extends Fragment {

    private Firebase mRef;
    private RecyclerView mChatRecyclerView;
    private FirebaseRecyclerAdapter<ChatModel, ChatHolder> mFirebaseRecycleViewAdapter;
    private EditText tvMessageToSend;
    private ImageView ivSendMessage;
    String userNmae;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_chat, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRef = new Firebase("https://ndtvchat.firebaseio.com/data/chat");
        userNmae = NDTVSharedPreferencesManager.getString(getActivity(), AppConstants.USER_NAME);

        mChatRecyclerView = (RecyclerView) view.findViewById(R.id.rvChat);
        tvMessageToSend = (EditText) view.findViewById(R.id.tvMessageToSend);
        ivSendMessage = (ImageView) view.findViewById(R.id.ivSendMessage);

        mChatRecyclerView.setLayoutManager(manager);
        mChatRecyclerView.setHasFixedSize(false);

        mFirebaseRecycleViewAdapter = new FirebaseRecyclerAdapter<ChatModel, ChatHolder>(ChatModel.class, R.layout.item_chat_row, ChatHolder.class, mRef) {
            @Override
            protected void populateViewHolder(ChatHolder chatHolder, ChatModel chatModel, int i) {
                chatHolder.tvName.setText(chatModel.getmName());
                chatHolder.tvMessage.setText(chatModel.getmMessage());
            }
        };

        ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel model = new ChatModel();
                model.setmName(userNmae);
                model.setmMessage(tvMessageToSend.getText().toString());
                mRef.push().setValue(model);
                tvMessageToSend.setText("");
            }
        });

        mChatRecyclerView.setAdapter(mFirebaseRecycleViewAdapter);
    }

    /**
     * Created by Shivam on 3/27/2016.
     */
    public static class ChatHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvMessage;

        public ChatHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvChatName);
            tvMessage = (TextView) itemView.findViewById(R.id.tvChatMessage);
        }

    }
}
