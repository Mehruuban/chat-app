package com.mehru.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;
import com.mehru.chatapp.Utils.AndroidUtil;
import com.mehru.chatapp.Utils.FirebaseUtils;
import com.mehru.chatapp.model.UserModel;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    ImageView profilePic;
    EditText profileUserNameInput;
    EditText profileUserPhoneInput;
    AppCompatButton updateBtn;
    TextView logoutBtn;
    ProgressBar progressBar ;

    UserModel currentUsrModel;
    ActivityResultLauncher <Intent> imagePickerLauncher;
    Uri selectedImageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                     if (result.getResultCode()== Activity.RESULT_OK){
                         Intent data = result.getData();
                         if (data!=null && data.getData()!=null){
                             selectedImageUri = data.getData();
                             AndroidUtil.setProfilePic(getContext(),selectedImageUri,profilePic);
                         }
                     }
                }
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = view.findViewById(R.id.profile_photo);
        profileUserNameInput =view.findViewById(R.id.profile_userName);
        profileUserPhoneInput =view.findViewById(R.id.profile_PhoneNo);
        updateBtn = view.findViewById(R.id.profile_update_btn);
        progressBar = view.findViewById(R.id.profile_progressbar);
        logoutBtn = view.findViewById(R.id.logout_btn);
        getUserData();

        updateBtn.setOnClickListener(v -> {
            updateBtnClick();
        });

        logoutBtn.setOnClickListener(v -> {

            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        FirebaseUtils.logout();
                        Intent intent = new Intent(getContext(), Splash_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            });
        });

        profilePic.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickerLauncher.launch(intent);
                            return null;
                        }
                    });
        });
        return view;


    }

    void updateBtnClick(){
        String newUsrName = profileUserNameInput.getText().toString();
        if (newUsrName.isEmpty() || newUsrName.length()<3){
            profileUserPhoneInput.setError("Usr Name Length Should At least 3 chars ");
            return;
        }
        currentUsrModel.setUserName(newUsrName);
        setInProgress(true);

        if (selectedImageUri!=null){
            AndroidUtil.getCurrentProfilePicStorageReference().putFile(selectedImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            updateToFireStore();
                        }
                    });
        }else {
            updateToFireStore();
        }
    }

    void updateToFireStore(){
        FirebaseUtils.currentUserDetails().set(currentUsrModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void getUserData(){
        setInProgress(true);

        FirebaseUtils.getCurrentProfilePicStorageReference().getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()){
                                    Uri uri = task.getResult();AndroidUtil.setProfilePic(getContext(),uri,profilePic);
                                }
                            }
                        });

        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUsrModel=task.getResult().toObject(UserModel.class);
            profileUserNameInput.setText(currentUsrModel.getUserName());
            profileUserPhoneInput.setText(currentUsrModel.getPhoneNumber());
        });
    }



    void setInProgress(boolean inProgress){
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            updateBtn.setVisibility(View.GONE);
        }else {

        }progressBar.setVisibility(View.GONE);
        updateBtn.setVisibility(View.VISIBLE);


    }
}