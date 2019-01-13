package com.example.user.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.task.adapter.CategoryDataAdapter;
import com.example.user.task.adapter.NewsDataAdapter;
import com.example.user.task.database.DbHelper;
import com.example.user.task.model.Category;
import com.example.user.task.model.Costs;

import java.util.ArrayList;
import java.util.List;



public class  MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mMainList;
    private NewsDataAdapter mNewsDataAdapter;
    private List<Costs> costsList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Главная");

        mMainList = findViewById(R.id.main_list);
        costsList = getCategoryAndSumCosts();
        mNewsDataAdapter = new NewsDataAdapter(this, costsList);
        mMainList.setAdapter(mNewsDataAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateNewsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        //dbHelper.onUpgrade(database, 1, 1);
    }


    protected ArrayList<Costs> getCategoryAndSumCosts() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<Costs> data = new ArrayList<>();

        String query = "select CATEGORY, NEWS from News co, Category ca "
                + "where co.CATEGORY = ca.NAME group by CATEGORY ;";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int categoryIndex = cursor.getColumnIndex("CATEGORY");
            int sumIndex = cursor.getColumnIndex("NEWS");
            //int dateIndex=cursor.getColumnIndex("DATE");
            do {
                data.add(new Costs(data.toString(),cursor.getString(categoryIndex), cursor.getString(sumIndex)/*,cursor.getString(dateIndex))*/));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                onBackPressed();
                break;

            case R.id.nav_costs_main:
                startActivity(new Intent(MainActivity.this, NewsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_costs_web:
                startActivity(new Intent(MainActivity.this, WebActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

        }
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

