package com.mehru.chatapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mehru.chatapp.model.UserModel;

public class AndroidUtil {

    public static void passUserModelAsIntent (Intent intent, UserModel userModel){
        intent.putExtra("userName",userModel.getUserName());
        intent.putExtra("phoneNumber",userModel.getPhoneNumber());
        intent.putExtra("userId",userModel.getUserId());
        intent.putExtra("fcmToken",userModel.getFcmToken());

    }

    public static  UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUserName(intent.getStringExtra("userName"));
        userModel.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;

    }

    public static  void setProfilePic(Context context , Uri imageUri , ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);

    }
    public static StorageReference getCurrentProfilePicStorageReference(){
        return FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(FirebaseUtils.currentUserId());

    }


}
