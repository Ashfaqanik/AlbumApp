package com.example.albums.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.albums.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText mobileNumber;
    private EditText password;
    private Button submitButton;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        mobileNumber = findViewById(R.id.number);
        password = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.register_progress_bar);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = firstName.getText().toString().trim();
                String lName = lastName.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Mobile = mobileNumber.getText().toString().trim();
                String pass = password.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                if(!fName.isEmpty() && !lName.isEmpty() && !Email.isEmpty() && !Mobile.isEmpty() && !pass.isEmpty())
                {
                    createUser();

                }else
                {
                    Toast.makeText(RegisterActivity.this,"Fill the requirements",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }


            }
        });
    }

    private void createUser() {
        final String fName = firstName.getText().toString();
        final String lName = lastName.getText().toString();
        final String Email = email.getText().toString().trim();
        final String Mobile = mobileNumber.getText().toString();
        final String pass = password.getText().toString().trim();
        Toast.makeText(this, "Registration in progress...", Toast.LENGTH_SHORT).show();
        firebaseAuth.createUserWithEmailAndPassword(Email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = firebaseAuth.getCurrentUser();
                    assert currentUser != null;
                    final String currentUserId = currentUser.getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", currentUserId);
                    user.put("firstName", fName);
                    user.put("lastName", lName);
                    user.put("email", Email);
                    user.put("mobile", Mobile);
                    user.put("password", pass);
                    //save to our firestore database
                    collectionReference.document(currentUserId).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    intent.putExtra("userId", currentUserId);
                                    startActivity(intent);
                                    RegisterActivity.this.finish();


                                }
                            }).
                            addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this,"User creation not succeed",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                } else {
                    Toast.makeText(RegisterActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,"Registration not succeed",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        //firebaseAuth.addAuthStateListener(authStateListener);

    }

}