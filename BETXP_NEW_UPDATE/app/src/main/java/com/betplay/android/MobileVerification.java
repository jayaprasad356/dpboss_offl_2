package com.betplay.dpboss_off;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileVerification extends AppCompatActivity {

    private EditText otp1;
    private EditText otp2;
    private EditText otp3;
    private EditText otp4;
    private EditText otp5;
    private EditText otp6;
    private TextView verify;
    private TextView resendButton;

    String mobileNumber = "", otp = "";
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String otpId;
    PhoneAuthProvider.ForceResendingToken otpToken;

    ViewDialog viewDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        context = this;
        initViews();
        mAuth = FirebaseAuth.getInstance();
        mobileNumber = getIntent().getStringExtra("mobile");

        otp1.addTextChangedListener(new GenericTextWatcher(otp1));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4));
        otp5.addTextChangedListener(new GenericTextWatcher(otp5));
        otp6.addTextChangedListener(new GenericTextWatcher(otp6));

        sendOTP();

        verify.setOnClickListener(view -> {
            if (otp == null) return;
            if (getOtp().isEmpty() || getOtp().length() != 6){
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, getOtp());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent=new Intent();
                                intent.putExtra("verification","success");
                                setResult(RESULT_OK,intent);
                                finish();
                            } else {

                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(context, "Incorrect OTP entered", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(context, "Unable to verify please retry later", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

        });

        resendButton.setOnClickListener(view -> {
            if (resendButton.getText().toString().equals(getString(R.string.resend_otp))) {
                if (otpToken != null && otpId != null) {

                    resendButton.setText("Sending");
                    resendButton.setOnClickListener(v -> Toast.makeText(context, "Sending OTP", Toast.LENGTH_SHORT).show());

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+"+mobileNumber)       // Phone number to verify
                                    .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity((Activity) context)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .setForceResendingToken(otpToken)
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            } else {
                Toast.makeText(this, "Wait before resend", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendOTP(){

        viewDialog = new ViewDialog((AppCompatActivity) context);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+mobileNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    public String getOtp(){
        return otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
    }

    private void initViews() {
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        verify = findViewById(R.id.verify);
        resendButton = findViewById(R.id.resend_button);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Intent intent=new Intent();
                intent.putExtra("verification","success");
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpId = verificationId;
                Log.e("cs",verificationId);
                otp = verificationId;
                otpToken = token;

                Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show();

//                viewDialog.hideDialog();

                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        resendButton.setText("wait " + millisUntilFinished / 1000+" sec");
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        resendButton.setText(getString(R.string.resend_otp));
                    }

                }.start();

            }
        };
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otp1:
                    if (text.length() == 1)
                        otp2.requestFocus();
                    break;
                case R.id.otp2:
                    if (text.length() == 1)
                        otp3.requestFocus();
                    else if (text.length() == 0)
                        otp1.requestFocus();
                    break;
                case R.id.otp3:
                    if (text.length() == 1)
                        otp4.requestFocus();
                    else if (text.length() == 0)
                        otp2.requestFocus();
                    break;
                case R.id.otp4:
                    if (text.length() == 1)
                        otp5.requestFocus();
                    else if (text.length() == 0)
                        otp3.requestFocus();
                    break;
                case R.id.otp5:
                    if (text.length() == 1)
                        otp6.requestFocus();
                    else if (text.length() == 0)
                        otp3.requestFocus();
                    break;
                case R.id.otp6:
                    if (text.length() == 0)
                        otp5.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            String text = arg0.toString();
            switch (view.getId()) {

                case R.id.otp1:
                    if (text.length() == 1)
                        otp2.requestFocus();
                    break;
                case R.id.otp2:
                    if (text.length() == 1)
                        otp3.requestFocus();
                    else if (text.length() == 0)
                        otp1.requestFocus();
                    break;
                case R.id.otp3:
                    if (text.length() == 1)
                        otp4.requestFocus();
                    else if (text.length() == 0)
                        otp2.requestFocus();
                    break;
                case R.id.otp4:
                    if (text.length() == 1)
                        otp5.requestFocus();
                    else if (text.length() == 0)
                        otp3.requestFocus();
                    break;
                case R.id.otp5:
                    if (text.length() == 1)
                        otp6.requestFocus();
                    else if (text.length() == 0)
                        otp4.requestFocus();
                    break;
                case R.id.otp6:
                    if (text.length() == 0)
                        otp5.requestFocus();
                    break;
            }
        }
    }
}