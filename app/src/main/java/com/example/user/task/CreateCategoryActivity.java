package com.example.user.task;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.task.database.DbHelper;
import com.example.user.task.model.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class CreateCategoryActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText mCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_category);

        mCategoryName = findViewById(R.id.category_name);
    }

    public void addCategory(View view) {
        String categoryName = mCategoryName.getText().toString();
        if (!categoryName.isEmpty()) {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("NAME", categoryName);
            database.insert("Category", null, cv);
            super.onBackPressed();

            String id = auth.getUid();
            Category user = new Category( mCategoryName.getText().toString() );
            FirebaseDatabase.getInstance().getReference().child("users").child(id == null ? "0" : id).setValue(user);
        } else {
            Toast.makeText(CreateCategoryActivity.this, "Введите название категории", Toast.LENGTH_SHORT).show();
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
                        CreateCategoryActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}