package com.mehru.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mehru.chatapp.Utils.FirebaseUtils;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;
    FrameLayout frameLayout;

    ChatFragment chatFragment ;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchButton = findViewById(R.id.main_search_btn);
        frameLayout = findViewById(R.id.main_frame_layout);

        // 💥 Initialize fragments here
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, searchUserActivity.class));
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_chat) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frame_layout, chatFragment)
                            .commit();
                }
                if (item.getItemId() == R.id.menu_profile) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frame_layout, profileFragment)
                            .commit();
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.menu_chat);

        getFCMToken();
    }

    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    String token = task.getResult();
                    //Log.i("humerus Token" ,token);
                    FirebaseUtils.currentUserDetails().update("fcmToken",token);
                }

            }
        });
    }
}