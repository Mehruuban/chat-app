package com.mehru.chatapp;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mehru.chatapp.Adapter.SearchUserRecyclerAdapter;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.UserModel;

import java.util.Queue;

public class searchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView ;

    SearchUserRecyclerAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_user);

        searchInput = findViewById(R.id.search_username_edittext);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recyclerView);

        searchInput.requestFocus();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchButton.setOnClickListener(v -> {

            String searchTerm = searchInput.getText().toString();
            if (searchTerm.isEmpty() || searchTerm.length()<3 ){
                searchInput.setError("Invalid UserName");
                return;
            }
            setUpSearchRecyclerView (searchTerm);


        });
    }

    void setUpSearchRecyclerView(String searchTerm){

        Query query = FirebaseUtils.UsercollectionReference()
                .whereGreaterThanOrEqualTo("userName",searchTerm)
                .whereLessThanOrEqualTo("userName",searchTerm+ '\uf8ff');

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
        if (adapter!= null){
            adapter.stopListening();
        }
        if (recyclerView!=null){
            recyclerView.setAdapter(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.startListening();
    }
}