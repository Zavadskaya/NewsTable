package com.example.user.task;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.task.database.DbHelper;
import com.example.user.task.model.Costs;
import com.example.user.task.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CreateNewsActivity extends AppCompatActivity {

    EditText mCost;
    Spinner mCategoryList;
    private FirebaseAuth auth;
    Button added;
    FirebaseUser user;
    User person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_costs);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mCost = findViewById(R.id.cost);
        mCategoryList = findViewById(R.id.category_list_spinner);
        added = findViewById(R.id.added);
        added.setEnabled(true);
    }

    @Override
    protected void onStart() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                getCategory());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategoryList.setAdapter(adapter);
        super.onStart();
    }

    protected ArrayList<String> getCategory() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select NAME from Category", null);
        if (cursor.moveToFirst()) {
            int indexName = cursor.getColumnIndex("NAME");
            do {
                data.add(cursor.getString(indexName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public void addCost(View view) {
        String cost = mCost.getText().toString();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        String selectedCategory = mCategoryList.getSelectedItem().toString();

        if (!cost.isEmpty()) {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            String ided = auth.getUid();
            ContentValues cv = new ContentValues();
            cv.put("NEWS", cost);
            cv.put("DATE", date);
            cv.put("CATEGORY", selectedCategory);
            cv.put("USERNAME", user.getUid());
            Costs news = new Costs(ided, mCost.getText().toString(), mCategoryList.getSelectedItem().toString());

            /*long id = */database.insert("News", null, cv);
            FirebaseDatabase.getInstance().getReference().child("users").child(ided == null ? "0" : ided).setValue(news);

            //Log.d("addCost", Long.toString(id));

            super.onBackPressed();
        } else {
            Toast.makeText(CreateNewsActivity.this, "Введите название категории", Toast.LENGTH_SHORT).show();
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
                        CreateNewsActivity.super.onBackPressed();
                    }
                }).create().show();

    }
}
