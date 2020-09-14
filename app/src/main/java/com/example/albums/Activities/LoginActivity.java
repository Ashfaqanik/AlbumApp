package com.example.albums.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.albums.R;
import com.example.albums.Utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView email;
    private EditText pass;
    private Button loginButton;
    private TextView signUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.Email);
        pass = findViewById(R.id.pass);
        loginButton=findViewById(R.id.login_button);
        signUp = findViewById(R.id.sign_UpText);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        if (PreferenceUtils.getEmail(this) != null ){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }else {

        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginEmailPasswordUser(email.getText().toString().trim(),
                        pass.getText().toString().trim());
            }
        });

    }
    private void loginEmailPasswordUser(final String Email, final String pwd) {

        Toast.makeText(this, "Login in progress", Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(Email)
                && !TextUtils.isEmpty(pwd)) {
            firebaseAuth.signInWithEmailAndPassword(Email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                assert user != null;
                                final String currentUserId = user.getUid();
                                collectionReference.document(currentUserId)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                if (e!=null){
                                                    Toast.makeText(LoginActivity.this,"Logged out", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    PreferenceUtils.saveEmail(Email, LoginActivity.this);
                                                    PreferenceUtils.savePassword(pwd, LoginActivity.this);

                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    finish();
                                                    startActivity(intent);
                                                }


                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


        } else {

            Toast.makeText(LoginActivity.this,
                    "Please enter email and password",
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        currentUser= null;
    }
}