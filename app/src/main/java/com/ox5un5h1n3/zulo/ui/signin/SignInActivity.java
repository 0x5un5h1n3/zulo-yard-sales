package com.ox5un5h1n3.zulo.ui.signin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
//    private MaterialButton btnForgotPassword, signUp, signUpSignIn;
    private ProgressBar progressBar;

    private MaterialAlertDialogBuilder reset_alert;
    LayoutInflater inflater;

    private FirebaseAuth mAuth;
//    private SignUpActivity registerViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        reset_alert = new MaterialAlertDialogBuilder(this);
        inflater = this.getLayoutInflater();


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

            View pop_view = inflater.inflate(R.layout.reset_pass_popup, null);

//            reset_alert.setTitle("Reset forgotten password")
            reset_alert.setTitle("Reset forgotten password")
                    .setMessage("Enter your email to receive password reset link")
                    .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //validate email address
                            EditText email = pop_view.findViewById(R.id.reset_email_popup);

                            if (email.getText().toString().isEmpty()){
                                email.setError("Required Field!");
                                return;
                            }
                            //send the reset link
                            mAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignInActivity.this, "Reset email sent!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel", null)
                    .setView(pop_view)
                    .create().show();
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

        else if(!email.contains("@")){
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        else if(pass.isEmpty()){
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
                    Toast.makeText(SignInActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
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


