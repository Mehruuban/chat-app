package com.mehru.chatapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.mehru.chatapp.ChatActivity;
import com.mehru.chatapp.R;
import com.mehru.chatapp.Utils.AndroidUtil;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.ChatMessageModel;

public class ChatRecyclerAdapter  extends FirestoreRecyclerAdapter<ChatMessageModel,ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context ;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context ) {
        super(options);
        this.context= context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int i, @NonNull ChatMessageModel chatMessageModel) {
        if (chatMessageModel.getSenderId().equals(FirebaseUtils.currentUserId())){
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.Right_chat_edittext.setText(chatMessageModel.getMessage());
        }else {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.left_chat_edittext.setText(chatMessageModel.getMessage());
        }

    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row,parent , false);
        return new ChatModelViewHolder(view);
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftChatLayout,rightChatLayout;
        TextView left_chat_edittext , Right_chat_edittext;


        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.Right_chat_layout);
            left_chat_edittext = itemView.findViewById(R.id.left_chat_textView);
            Right_chat_edittext = itemView.findViewById(R.id.right_chat_textView);
        }

    }
}

