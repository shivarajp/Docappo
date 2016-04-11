package com.ndtv.ndtvdoc.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.ndtv.ndtvdoc.R;
import com.ndtv.ndtvdoc.fragments.ChatFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Firebase.setAndroidContext(this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        ChatFragment fragment = new ChatFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment).commit();

    }
}
