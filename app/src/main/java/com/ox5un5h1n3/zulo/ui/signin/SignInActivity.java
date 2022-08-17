package com.ox5un5h1n3.zulo.ui.signin;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.MainActivity;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.UserDetail;
import com.ox5un5h1n3.zulo.databinding.ActivitySigninBinding;
import com.ox5un5h1n3.zulo.ui.signup.SignUpActivity;

public class SignInActivity extends AppCompatActivity {

    LayoutInflater inflater;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private MaterialAlertDialogBuilder reset_alert;
    private String userId;


    private FirebaseAuth firebaseAuth;
    private SignInClient signInClient;


    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    handleSignInResult(result.getData());
                }
            }
    );

    private void handleSignInResult(Intent intent) {
        try {
            SignInCredential credential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = credential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);

        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Sign in Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    userId = firebaseAuth.getCurrentUser().getUid();
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    final UserDetail userDetail = new UserDetail(user.getDisplayName(), user.getEmail(), userId, "", "");
                    FirebaseFirestore.getInstance().collection("Users").document(userId).set(userDetail);
                    finish();
                    startActivity((new Intent(SignInActivity.this, MainActivity.class)));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Sign up Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }


                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(SignInActivity.this, user.getDisplayName() + " signed in!", Toast.LENGTH_SHORT).show();
                goToMainActivity(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void goToMainActivity(FirebaseUser user) {
        if (user != null) {
            finish();
            startActivity((new Intent(SignInActivity.this, MainActivity.class)));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Configure Google SignInClient
        signInClient = Identity.getSignInClient(getApplicationContext());

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        reset_alert = new MaterialAlertDialogBuilder(this);
        inflater = this.getLayoutInflater();


        super.onCreate(savedInstanceState);

        ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button signInButton = (Button) binding.btnSignIn;
        final Button signInSignUpButton = binding.btnSignInSignUp;
        final Button forgotButton = binding.btnForgotPassword;
        final Button signInWithGoogleButton = binding.btnSignInWithGoogle;

        signInSignUpButton.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        signInButton.setOnClickListener(view -> {
            signInUser();

        });

        signInWithGoogleButton.setOnClickListener(view -> {
            signInWithGoogle();
        });

        // if you forgot your password
        forgotButton.setOnClickListener(view -> {

            View pop_view = inflater.inflate(R.layout.dialog_reset_pass, null);

            reset_alert.setTitle("Reset forgotten password")
                    .setMessage("Enter your email to receive password reset link")
                    .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //validate email address
                            EditText email = pop_view.findViewById(R.id.reset_email_popup);

                            if (email.getText().toString().isEmpty()) {
                                email.setError("Required Field!");
                                return;
                            }
                            //send the reset link
                            firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        goToMainActivity(user);
    }

    private void signInWithGoogle() {

        GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                .setServerClientId(getString(R.string.web_client_id)).build();

        Task<PendingIntent> signInIntent = signInClient.getSignInIntent(signInIntentRequest);
        signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
            @Override
            public void onSuccess(PendingIntent pendingIntent) {
                launchSignIn(pendingIntent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Sign in Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void launchSignIn(PendingIntent pendingIntent) {
        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent).build();
        signInLauncher.launch(intentSenderRequest);
    }


    private void signInUser() {
        editTextEmail = findViewById(R.id.emailAddress);
        editTextPassword = findViewById(R.id.pass);

        progressBar = findViewById(R.id.loading);

        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();


        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        } else if (!email.contains("@")) {
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        } else if (pass.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

//                  email verification
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    assert user != null;
                    if(user.isEmailVerified()){
                        // redirect to MainActivity
                        progressBar.setVisibility(View.VISIBLE);
                        finish();
                        Toast.makeText(SignInActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
                        startActivity((new Intent(SignInActivity.this, MainActivity.class)));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(SignInActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }


//                    // redirect to MainActivity (without email verification)
//                    progressBar.setVisibility(View.VISIBLE);
//                    finish();
//                    Toast.makeText(SignInActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();
//                    startActivity((new Intent(SignInActivity.this, MainActivity.class)));


                } else {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Sign in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}


