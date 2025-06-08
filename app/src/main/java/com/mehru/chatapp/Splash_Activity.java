package com.mehru.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mehru.chatapp.Utils.AndroidUtil;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.UserModel;

public class Splash_Activity extends AppCompatActivity {
    Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if ( FirebaseUtils.isLoggedIn() && getIntent().getExtras()!=null){
            //from notification
            String userId =getIntent().getExtras().getString("userId");

            FirebaseUtils.UsercollectionReference().document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        UserModel userModel = task.getResult().toObject(UserModel.class);

                        Intent nextIntent = new Intent(context,MainActivity.class);
                        nextIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(nextIntent);

                        Intent intent = new Intent(context, ChatActivity.class);
                        AndroidUtil.passUserModelAsIntent(intent,userModel);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (FirebaseUtils.isLoggedIn()) {
                        startActivity(new Intent(Splash_Activity.this, MainActivity.class));
                    }else {
                        startActivity(new Intent(Splash_Activity.this,LoginPhoneNoActivity.class));
                    }
                    finish();

                }
            },1000);
        }

    }
}