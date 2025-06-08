package com.mehru.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    String phoneNumber;
    Long timeoutSecond = 60L;
    String verificationCode ;
    PhoneAuthProvider.ForceResendingToken resendingToken ;

    EditText otpInput ;
    AppCompatButton nextBtn;
    ProgressBar progressBar;
    TextView   resendOtpTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otp);



        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progressBar);
        resendOtpTextView = findViewById(R.id.resend_otp_textView);

        phoneNumber = getIntent().getExtras().getString("phone");

        sendOtp(phoneNumber,false);
        // Toast.makeText(this, "Otp sent Successfully ", Toast.LENGTH_LONG).show();

        // code for testing the firebase working or not
//        Map<String,String > data = new HashMap<>();
//        FirebaseFirestore.getInstance().collection("test").add(data);

        nextBtn.setOnClickListener(v -> {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
            signIn(credential);
            setInProgress(true);

        });

        resendOtpTextView.setOnClickListener(v -> {
            sendOtp(phoneNumber,true);

        });



    }
    //Method for send the otp
    void sendOtp(String phoneNumber,boolean isResend){
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSecond, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(LoginOtpActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                Log.d("OTP_VERIFICATION_CODE", s); // for debug only
                                Toast.makeText(LoginOtpActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                                setInProgress(false);
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    void signIn (PhoneAuthCredential phoneAuthCredential){
        //Loin and go to next activity
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);

                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginOtpActivity.this,LoginUserNameActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginOtpActivity.this, "OTP Verification Failed    ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    void startResendTimer(){
        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
               timeoutSecond --;
               resendOtpTextView.setText("Resend OTP In " +timeoutSecond + " Second");

               if (timeoutSecond<0){
                   timeoutSecond = 60L;
                   timer.cancel();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           resendOtpTextView.setEnabled(true);

                       }
                   });

               }
            }
        },0,1000);
    }
}