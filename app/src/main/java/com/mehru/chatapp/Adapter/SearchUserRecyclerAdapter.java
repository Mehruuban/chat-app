package com.mehru.chatapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mehru.chatapp.ChatActivity;
import com.mehru.chatapp.R;
import com.mehru.chatapp.Utils.AndroidUtil;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.UserModel;

public class SearchUserRecyclerAdapter  extends FirestoreRecyclerAdapter<UserModel,SearchUserRecyclerAdapter.userModelViewHolder> {

    Context context ;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context ) {
        super(options);
        this.context= context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull userModelViewHolder holder, int i, @NonNull UserModel userModel) {
         holder.userNameText.setText(userModel.getUserName());
         holder.phoneText.setText(userModel.getPhoneNumber());

         if (userModel.getUserId().equals(FirebaseUtils.currentUserId())){
             holder.userNameText.setText(userModel.getUserName()+"(Me)");
       }

        FirebaseUtils.getOtherProfilePicStorageReference(userModel.getUserId()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri uri = task.getResult();AndroidUtil.setProfilePic(context,uri,holder.profilePic);
                        }
                    }
                });


         holder.itemView.setOnClickListener(v -> {
             //navigate to chat activity

             Intent intent = new Intent(context, ChatActivity.class);
             AndroidUtil.passUserModelAsIntent(intent,userModel);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(intent);

         });

    }

    @NonNull
    @Override
    public userModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycletr_raw,parent , false);
        return new userModelViewHolder(view);
    }

    class userModelViewHolder extends RecyclerView.ViewHolder{
        TextView userNameText ;
        TextView phoneText;
        ImageView profilePic;


        public userModelViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameText = itemView.findViewById(R.id.username_text);
            phoneText= itemView.findViewById(R.id.user_phone_text);
            profilePic = itemView.findViewById(R.id.profile_image);
        }

    }
}
