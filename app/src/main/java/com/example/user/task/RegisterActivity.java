package com.example.user.task;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.task.database.DbHelper;
import com.example.user.task.model.Category;
import com.example.user.task.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private EditText email;
    private EditText password;
    private EditText firstName, secondName,
            groupUser, courseUser, facultyUser;
    private CheckBox isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    public void init() {
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firstName = findViewById(R.id.firstname);
        secondName = findViewById(R.id.secondname);
        courseUser = findViewById(R.id.course);
        groupUser = findViewById(R.id.group);
        facultyUser = findViewById(R.id.faculty);
        isAdmin = findViewById(R.id.isAdmin);
        findViewById(R.id.registration).setOnClickListener(this);
    }


    private boolean IsCorrect(String emails) {
        if (emails.isEmpty()) {
            email.setError("Введите email");
            email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("Введите корректный email");
            email.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean IsCorrectPass(String passwords) {
        if (passwords.isEmpty()) {
            password.setError("Введите пароль");
            password.requestFocus();
            return false;
        } else if (password.length() < 6) {
            password.setError("Пароль должен быть больше 6 символов");
            return false;
        } else {
            return true;
        }
    }

    private void createUser() {
        String emails = email.getText().toString().trim();
        String passwords = password.getText().toString().trim();
        String name = firstName.getText().toString();
        String surname = secondName.getText().toString();
        String group = groupUser.getText().toString();
        String course = courseUser.getText().toString();
        String faculty = facultyUser.getText().toString();
        if (!IsCorrect(emails) && (!IsCorrectPass(passwords))) {
            return;
        }
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("keys", auth.getCurrentUser().getUid());
        cv.put("firstname", name);
        cv.put("secondname", surname);
        cv.put("course", course);
        cv.put("groupe", group);
        cv.put("faculty", faculty);
        cv.put("email", emails);
        database.insert("User", null, cv);
    }

    private void registerUser() {
        String emails = email.getText().toString().trim();
        String passwords = password.getText().toString().trim();
        super.onBackPressed();

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(emails, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    String id = auth.getCurrentUser().getUid();
                    User user = new User(firstName.getText().toString(),
                            secondName.getText().toString(),
                            groupUser.getText().toString(),
                            courseUser.getText().toString(),
                            facultyUser.getText().toString(),
                            email.getText().toString(), isAdmin.isChecked());
                    FirebaseDatabase.getInstance().getReference().child("users").child(id == null ? "0" : id).setValue(user);
                    createUser();
                    /*startActivity(new Intent(RegisterActivity.this, CreateUserActivity.class));
                    finish();*/
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Вы уже зарегистрировались", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration:
                registerUser();
                Toast.makeText(getApplicationContext(), "Регистрация выполнена успешно!", Toast.LENGTH_SHORT).show();
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
                        RegisterActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
