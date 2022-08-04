package com.ox5un5h1n3.zulo.ui.signin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ox5un5h1n3.zulo.MainActivity;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.databinding.ActivitySigninBinding;
import com.ox5un5h1n3.zulo.databinding.ActivitySignupBinding;
import com.ox5un5h1n3.zulo.ui.signup.SignUpActivity;

public class SignInActivity extends AppCompatActivity{

    private EditText editTextEmail, editTextPassword;
//    private MaterialButton signUp, signUpSignIn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
//    private SignUpActivity registerViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();


//        signUp = (MaterialButton) findViewById(R.id.btnSignUp);
//        signUp.setOnClickListener(SignUpActivity.this);

//        signUpSignIn = (MaterialButton) findViewById(R.id.btnSignUpSignIn);
//        signUpSignIn.setOnClickListener(SignUpActivity.this);

//        editTextUsername = (EditText) findViewById(R.id.username);
//        editTextEmail = (EditText) findViewById(R.id.emailAddress);
//        editTextPassword = (EditText) findViewById(R.id.pass);
//        editTextRePassword = (EditText) findViewById(R.id.rePass);


        super.onCreate(savedInstanceState);

        ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button signInButton = (Button) binding.btnSignIn;
        final Button signInSignUpButton = binding.btnSignInSignUp;
        final Button forgotButton = binding.btnForgotPassword;

        signInSignUpButton.setOnClickListener(view -> {
//            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
//
//            dialog.setMessage("A temporary password will be sent to your inputted username/email earlier...");
//
//            dialog.setNegativeButton("Cancel", null);
//            dialog.setPositiveButton("Proceed", null);
//
//            dialog.show();
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        signInButton.setOnClickListener(view -> {
            signInUser();

        });

        // if you forgot your password
        forgotButton.setOnClickListener(view -> {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);

            dialog.setMessage("A link to reset your Zulo account password will be sent to your inputted email earlier...");

            dialog.setNegativeButton("Cancel", null);
            dialog.setPositiveButton("Proceed", null);

            dialog.show();
        });
    }

    private void signInUser() {
        editTextEmail = findViewById(R.id.emailAddress);
        editTextPassword = findViewById(R.id.pass);

        progressBar = findViewById(R.id.loading);

        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();




        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!email.contains("@")){
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // redirect to MainActivity
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(SignInActivity.this, "User signed in successfully!", Toast.LENGTH_SHORT).show();
                    startActivity((new Intent(SignInActivity.this, MainActivity.class)));
                } else {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Sign in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


//    private void showRegisterSuccess(RegisterUserState model) {
//        // Initiate successful logged in experience -> Home interface
//        Intent intent = new Intent(LoginActivity.this,
//                MainActivity.class);
//        startActivity(intent);
//    }

}


