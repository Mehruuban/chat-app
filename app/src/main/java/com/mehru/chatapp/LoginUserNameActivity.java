package com.mehru.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.UserModel;

public class LoginUserNameActivity extends AppCompatActivity {
    TextView userNameInput;
    AppCompatButton letMeInBtn;
    ProgressBar progressBar ;

    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_user_name);

        userNameInput = findViewById(R.id.login_userName);
        letMeInBtn = findViewById(R.id.login_complete_profile);
        progressBar = findViewById(R.id.login_progressBar);



        phoneNumber = getIntent().getExtras().getString("phone");
        getUserName();

        letMeInBtn.setOnClickListener(v -> {
            setUsername();
        });

    }


    void  setUsername(){
        String username = userNameInput.getText().toString();
        if (username.isEmpty() || username.length()<3) {
            userNameInput.setError("User Name At Least 3 Chars");
            return;
        }

        setInProgress(true);
        if (userModel!= null){
            userModel.setUserName(username);
        } else {
            userModel = new UserModel(phoneNumber,username, Timestamp.now(),FirebaseUtils.currentUserId());
        }

        FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginUserNameActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    void getUserName (){
        setInProgress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    userModel = task.getResult().toObject(UserModel.class);
                    if (userModel!=null){
                        userNameInput.setText(userModel.getUserName());
                    }
                }

            }
        });
    }




    void  setInProgress (boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            letMeInBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            letMeInBtn.setVisibility(View.VISIBLE);
        }

    }

}