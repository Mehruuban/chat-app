package com.mehru.chatapp;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNoActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker ;
    EditText phoneInput;
    AppCompatButton sendOtpBtn;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_phone_no);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        countryCodePicker = findViewById(R.id.login_countryCode);
        phoneInput = findViewById(R.id.login_mobileNumber);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progressBar);

        progressBar.setVisibility(GONE);


        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        sendOtpBtn.setOnClickListener((v)->{
            if (!countryCodePicker.isValidFullNumber()){
                phoneInput.setError("Phone Number Is Not Valid");

                String phone = countryCodePicker.getFullNumberWithPlus();
                Log.d("PHONE_DEBUG", "Phone: " + phone);
               // Log.d("SEND_OTP", countryCodePicker.getFullNumberWithPlus());

                return;
            }

            Intent intent =  new Intent(LoginPhoneNoActivity.this,LoginOtpActivity.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);


        });
    }
}