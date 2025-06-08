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
import com.google.firebase.firestore.DocumentSnapshot;
import com.mehru.chatapp.ChatActivity;
import com.mehru.chatapp.R;
import com.mehru.chatapp.Utils.AndroidUtil;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.ChatRoomModel;
import com.mehru.chatapp.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatFragRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel,ChatFragRecyclerAdapter.ChatFragRoomModelViewHolder> {

    Context context ;

    public ChatFragRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context ) {
        super(options);
        this.context= context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ChatFragRoomModelViewHolder holder, int i, @NonNull ChatRoomModel chatRoomModel) {
        FirebaseUtils.getOtherUserFromChatRoom(chatRoomModel.getUserIds())
                .get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        boolean lastMessageSendByMe =chatRoomModel.getLastMessageSender().equals(FirebaseUtils.currentUserId());

                        UserModel otheruserModel = task.getResult().toObject(UserModel.class);


                        FirebaseUtils.getOtherProfilePicStorageReference(otheruserModel.getUserId()).getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()){
                                            Uri uri = task.getResult();AndroidUtil.setProfilePic(context,uri,holder.profilePic);
                                        }
                                    }
                                });

                        if (otheruserModel!=null){
                            holder.userNameText.setText(otheruserModel.getUserName());

                        }else {
                            holder.userNameText.setText("UnknownUser");
                        }

                        if (lastMessageSendByMe)
                            holder.lastMessageText.setText("You : " + chatRoomModel.getLastMessage());
                            //holder.lastMessageText.setText(chatRoomModel.getLastMessage());
                        else
                            holder.lastMessageText.setText(chatRoomModel.getLastMessage());
                            //holder.lastMessageText.setText("You : " + chatRoomModel.getLastMessage());


                        if (chatRoomModel.getLastMessageTimeStamp() != null) {
                            Date date = chatRoomModel.getLastMessageTimeStamp().toDate();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                            String time = sdf.format(date);
                            holder.lastMessageTime.setText(time);
                        } else {
                            holder.lastMessageTime.setText("");
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //to chat activity
                                Intent intent = new Intent(context,ChatActivity.class);
                                AndroidUtil.passUserModelAsIntent(intent,otheruserModel);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        });
                    }
                });

    }

    @NonNull
    @Override
    public ChatFragRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_fragment_recycler_row,parent , false);
        return new ChatFragRoomModelViewHolder(view);
    }

    class ChatFragRoomModelViewHolder extends RecyclerView.ViewHolder{
        TextView userNameText ;
        TextView lastMessageText;
        TextView lastMessageTime;
        ImageView profilePic ;


        public ChatFragRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameText = itemView.findViewById(R.id.username_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            profilePic = itemView.findViewById(R.id.profile_image);
        }

    }
}
