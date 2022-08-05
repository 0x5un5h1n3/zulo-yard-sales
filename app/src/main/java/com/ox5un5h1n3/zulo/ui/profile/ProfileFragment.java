package com.ox5un5h1n3.zulo.ui.profile;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ox5un5h1n3.zulo.MainActivity;
import com.ox5un5h1n3.zulo.R;
import com.ox5un5h1n3.zulo.databinding.ActivityMainBinding;
import com.ox5un5h1n3.zulo.databinding.ActivitySignupBinding;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {




    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        username = (TextView) view.findViewById(R.id.profileUsename);
//        email = (TextView) view.findViewById(R.id.profileEmail);
//        phone = (TextView) view.findViewById(R.id.profilePhone);
//
//        fAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//
//        userId = fAuth.getCurrentUser().getUid();
//
//
//        DocumentReference documentReference  = fStore.collection("users").document(userId);
//        documentReference.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
//
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                username.setText(documentSnapshot.getString("username"));
//                email.setText(documentSnapshot.getString("email"));
//                phone.setText(documentSnapshot.getString("phone"));
//
//            }
//        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView username, email, phone, location;
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;
        String userId;

        username = view.findViewById(R.id.profileUsename);
        email = view.findViewById(R.id.profileEmail);
        phone = view.findViewById(R.id.profilePhone);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
                phone.setText(documentSnapshot.getString("phone"));
            }
        });

    }
}