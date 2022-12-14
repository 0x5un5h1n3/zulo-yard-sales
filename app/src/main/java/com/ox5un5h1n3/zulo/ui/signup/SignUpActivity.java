package com.ox5un5h1n3.zulo.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.MainActivity;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.UserDetail;
import com.ox5un5h1n3.zulo.databinding.ActivitySignupBinding;
import com.ox5un5h1n3.zulo.ui.signin.SignInActivity;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText editTextUsername, editTextPhone, editTextEmail, editTextPassword, editTextRePassword;
    private FirebaseFirestore firestore;
    private ProgressBar progressBar;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);

        ActivitySignupBinding binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button signUpButton = binding.btnSignUp;
        final Button signUpSignInButton = binding.btnSignUpSignIn;

        signUpSignInButton.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        signUpButton.setOnClickListener(view -> {
            signUpUser();
        });
    }

    private void signUpUser() {

        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.emailAddress);
        editTextPassword = findViewById(R.id.pass);
        editTextRePassword = findViewById(R.id.rePass);
        progressBar = findViewById(R.id.loading);

        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        String rePass = editTextRePassword.getText().toString().trim();

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Registering User");
        mDialog.setCancelable(false);


        if (username.isEmpty()) {
            editTextUsername.setError("Username is required!");
            editTextUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!email.contains("@")) {
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (rePass.isEmpty()) {
            editTextRePassword.setError("Repeat password is required!");
            editTextRePassword.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if (!pass.equals(rePass)) {
            editTextRePassword.setError("Repeat password doesn't match!");
            editTextRePassword.requestFocus();
        } else {

            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String userUid = task.getResult().getUser().getUid();
                                final UserDetail userDetail = new UserDetail(username, email, userUid, "", "");

                                FirebaseFirestore.getInstance().collection("Users").document(userUid).set(userDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        mDialog.cancel();
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                        Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mDialog.cancel();
                                        Toast.makeText(SignUpActivity.this, "Failed to register user \n" + e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                mDialog.cancel();
                                Log.e("createUserWithEmail", task.getException().getMessage());
                                Toast.makeText(SignUpActivity.this, "Authentication failed" + task.getResult(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}


