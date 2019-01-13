package com.example.user.task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.task.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FireBaseWindow extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private DatabaseReference users;
    HashMap<String, User> u = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_window);
        auth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance().getReference();
        users = db.child("users");

        mEmail = findViewById(R.id.editText_email);
        mPassword = findViewById(R.id.editText_password);
        mProgressBar = findViewById(R.id.progressBar);

        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.without_acc).setOnClickListener(this);

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    u.put(ds.getKey(), ds.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean emailIsCorrect(String email) {
        if (email.isEmpty()) {
            mEmail.setError("Введите email");
            mEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Введите корректный email");
            mEmail.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordIsCorrect(String password) {
        if (password.isEmpty()) {
            mPassword.setError("Введите пароль");
            mPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            mPassword.setError("Минимальная длина пароля составляет 6 симолов");
            mPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    private void userLogin() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (!emailIsCorrect(email) || !passwordIsCorrect(password)) {
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    User user = u.get(auth.getCurrentUser().getUid());
                    if (user.getAdmin()) {
                        startActivity(new Intent(FireBaseWindow.this, /*MainActivity*/BlockUsersActivity.class));
                    } else if (user.getBlocked()) {
                        Toast.makeText(getApplicationContext(), "Вы заблокированы", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(FireBaseWindow.this, NewsActivity.class));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                userLogin();
                break;
            case R.id.without_acc:
                startActivity(new Intent(this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        FireBaseWindow.super.onBackPressed();
                    }
                }).create().show();
    }
}
