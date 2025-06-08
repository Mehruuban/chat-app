package com.mehru.chatapp;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mehru.chatapp.Adapter.ChatFragRecyclerAdapter;
import com.mehru.chatapp.Adapter.SearchUserRecyclerAdapter;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.ChatMessageModel;
import com.mehru.chatapp.model.ChatRoomModel;
import com.mehru.chatapp.model.UserModel;


public class ChatFragment extends Fragment {
    RecyclerView recyclerView ;
    ChatFragRecyclerAdapter adapter ;

    public ChatFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.chat_fragment_recyclerView);

        setUpRecyclerView();
        return view ;
    }

    void setUpRecyclerView(){

        Query query = FirebaseUtils.allChatRoomCollectionReference()
                .whereArrayContains("userIds",FirebaseUtils.currentUserId())
                .orderBy("lastMessageTimeStamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query,ChatRoomModel.class).build();

        adapter = new ChatFragRecyclerAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.notifyDataSetChanged();
    }
}