package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText emailView, passwordView, usernameView;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        mAuth = FirebaseAuth.getInstance();

        emailView = (EditText) findViewById(R.id.emailTextView);
        passwordView = (EditText) findViewById(R.id.passwordTextView);
        usernameView = (EditText) findViewById(R.id.usernameText);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Teachers");
    }



    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(this, afterLogin.class);
        startActivity(intent);
    }

    public void createAccount(View view){
        final String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        final String username = usernameView.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            Teacher teacher = new Teacher(username, email);

                            myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        notifyUser("Registration success");
                                        updateUI(user);
                                    }
                                    else{
                                        notifyUser(task.getException().toString());
                                    }
                                }
                            });

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            notifyUser(task.getException().toString());

                            // ...
                        }
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    notifyUser("Invalid password");
                } else if (e instanceof FirebaseAuthInvalidUserException) {

                    String errorCode =
                            ((FirebaseAuthInvalidUserException) e).getErrorCode();

                    if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                        notifyUser("No matching account found");
                    } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                        notifyUser("User account has been disabled");
                    } else {
                        notifyUser(e.getLocalizedMessage());
                    }
                }

            }
        });

    }



    public void notifyUser(String string){
        Toast.makeText(EmailPasswordActivity.this, string,
                Toast.LENGTH_SHORT).show();
    }

    public void signIn(View view) {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            notifyUser("Authentication failed.");
                            // ...
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    notifyUser("Invalid password");
                } else if (e instanceof FirebaseAuthInvalidUserException) {

                    String errorCode =
                            ((FirebaseAuthInvalidUserException) e).getErrorCode();

                    if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                        notifyUser("No matching account found");
                    } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                        notifyUser("User account has been disabled");
                    } else {
                        notifyUser(e.getLocalizedMessage());
                    }
                }

            }
        });

    }


}
