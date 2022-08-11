package com.ox5un5h1n3.zulo.ui.signin;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ox5un5h1n3.zulo.MainActivity;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.data.model.UserDetail;
import com.ox5un5h1n3.zulo.databinding.ActivitySigninBinding;
import com.ox5un5h1n3.zulo.ui.signup.SignUpActivity;

public class SignInActivity extends AppCompatActivity{

    private EditText editTextEmail, editTextPassword;
    //    private MaterialButton btnForgotPassword, signUp, signUpSignIn;
    private ProgressBar progressBar;
    private MaterialAlertDialogBuilder reset_alert;
    //    private AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    private String userId;
    private FirebaseFirestore firestore;




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

        }catch (ApiException e) {
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
                if (task.isSuccessful()){
//
//
                    userId = firebaseAuth.getCurrentUser().getUid();
//                    DocumentReference documentReference  = firestore.collection("users").document(userId);
//
                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    updateUI(user);
                    final UserDetail userDetail = new UserDetail(user.getDisplayName(), user.getEmail(), userId, "","");
                    FirebaseFirestore.getInstance().collection("Users").document(userId).set(userDetail);

//
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    updateUI(user);
//
//                    Map<String, Object> mapUser = new HashMap<>();
//                    mapUser.put("username", user.getDisplayName());
//                    mapUser.put("email", user.getEmail());
//
//                    documentReference.set(mapUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(SignInActivity.this, "User Id"+ userId +"Display Name"+user.getDisplayName() , Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(SignInActivity.this, "User Id Failed"+ userId +"Display Name"+user.getDisplayName() , Toast.LENGTH_SHORT).show();
//
////                            Log.d(TAG , "onFailure: "+e.toString());
//                            Toast.makeText(SignInActivity.this, "User Id"+ userId +"Error"+e.toString(), Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                    finish();
                    startActivity((new Intent(SignInActivity.this, MainActivity.class)));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Sign up Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }





                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    Toast.makeText(SignInActivity.this, user.getEmail()+" signed in!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SignInActivity.this, user.getDisplayName()+" signed in!", Toast.LENGTH_SHORT).show();
                    goToMainActivity(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void goToMainActivity(FirebaseUser user) {
        if(user != null){
            finish();
            startActivity((new Intent(SignInActivity.this, MainActivity.class)));
        }
    }

//    private SignUpActivity registerViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Configure Google SignInClient
        signInClient = Identity.getSignInClient(getApplicationContext());

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        reset_alert = new MaterialAlertDialogBuilder(this);
        inflater = this.getLayoutInflater();



        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                              @Override
                                              public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                                                  if (firebaseAuth.getCurrentUser() != null) {
                                                      // already logged in, go to home screen
                                                      finish();
                                                      startActivity((new Intent(SignInActivity.this, MainActivity.class)));
                                                  } else {
                                                      // initiate sign-in
                                                      signInUser();
                                                  }
                                              }
                                          }
        );

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
        final Button signInWithGoogleButton = binding.btnSignInWithGoogle;

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

        signInWithGoogleButton.setOnClickListener(view -> {
            signInWithGoogle();
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

//            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
//
//            dialog.setMessage("A link to reset your Zulo account password will be sent to your inputted email earlier...");
//
//            dialog.setNegativeButton("Cancel", null);
//            dialog.setPositiveButton("Proceed", null);
//
//            dialog.show();
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

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    //email verification
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                    if(user.isEmailVerified()){
//                        // redirect to MainActivity
//                        progressBar.setVisibility(View.VISIBLE);
//                        Toast.makeText(SignInActivity.this, "User signed in successfully!", Toast.LENGTH_SHORT).show();
//                        startActivity((new Intent(SignInActivity.this, MainActivity.class)));
//                    }else{
//                        Toast.makeText(SignInActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
//
//                    }



                    // redirect to MainActivity
                    progressBar.setVisibility(View.VISIBLE);
                    finish();
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


