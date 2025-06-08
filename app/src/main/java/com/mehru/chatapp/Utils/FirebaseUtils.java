package com.mehru.chatapp.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FirebaseUtils {


    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if (currentUserId()!=null){
            return true;
        }else {
            return false;
        }
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference UsercollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static CollectionReference getChatRoomMessageReference(String chatroomId){
        return getChatRoomReference(chatroomId).collection("chats");
    }

    public static DocumentReference getChatRoomReference (String chatRoomId){
       return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    public  static CollectionReference allChatRoomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }


    public static StorageReference getCurrentProfilePicStorageReference(){
        return FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(FirebaseUtils.currentUserId());

    }

    public static StorageReference getOtherProfilePicStorageReference(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profilePic")
                .child(otherUserId);

    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
    public static DocumentReference getOtherUserFromChatRoom(List<String> userIds){
        if (userIds.get(0).equals(FirebaseUtils.currentUserId())){
          return  UsercollectionReference().document(userIds.get(1));

        }else {
            return UsercollectionReference().document(userIds.get(0));
        }
    }

    public static String getChatRoomId (String userId1, String userId2){


        if (userId1.hashCode()<userId2.hashCode()){
            return userId1+ "_"+userId2;
        }else
            return userId2+"_"+userId1;
    }

}
