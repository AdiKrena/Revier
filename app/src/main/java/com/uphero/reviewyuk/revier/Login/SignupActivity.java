/*
Licensed by 2YoungDev
Copyleft 2017-2018
Surabaya 09-07-2017
*/

package com.uphero.reviewyuk.revier.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uphero.reviewyuk.revier.Home.HomeActivity;
import com.uphero.reviewyuk.revier.R;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName;
    private Button btnSignIn, btnSignUp, btnResetPassword;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputName = (EditText) findViewById(R.id.etname);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startRegister();
            }
        });
    }

    public void startRegister(){
        final String name = inputName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if(inputName.getText().toString().contains(" ")){
            inputName.setError("No space allowed");
            Toast.makeText(this, "No space allowed", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(name) &&TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Some field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 8 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //create user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Authentication failed" + task.getException(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    String user_id = mAuth.getCurrentUser().getUid();

                    DatabaseReference current_db_user = mDatabaseUsers.child(user_id);

                    current_db_user.child("name").setValue(name);
                    current_db_user.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/revier-smkn1.appspot.com/o/Profile+images%2Fcropped-2006659817.jpg?alt=media&token=92600a3d-ba48-42f3-86b1-08902d7435b2");

                    Intent homeIntent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}