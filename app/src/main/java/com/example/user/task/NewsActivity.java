package com.example.user.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.task.adapter.NewsDataAdapter;
import com.example.user.task.database.DbHelper;
import com.example.user.task.model.Category;
import com.example.user.task.model.Costs;
import com.example.user.task.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView mCostsList;
    protected List<Costs> newsList;
    private NewsDataAdapter mNewsDataAdapter;
    private Costs costs;
    private ArrayList<String> news = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs);

        this.setTitle("История");

        Toolbar toolbar = findViewById(R.id.toolbar_costs);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_costs);
        navigationView.setNavigationItemSelectedListener(this);

        mCostsList = findViewById(R.id.costs_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                news);
        mCostsList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "Updating data");
        news.clear();
        news.addAll(getCosts());

        for (String i : news) {
            Log.d("onStart:news", i);
        }

        adapter.notifyDataSetChanged();
    }

    protected ArrayList<String> getCosts() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from News", null);
        if (cursor.moveToFirst()) {
            int indexCost = cursor.getColumnIndex("NEWS");
            int indexDate = cursor.getColumnIndex("DATE");
            int indexCategory = cursor.getColumnIndex("CATEGORY");
            do {
                data.add(cursor.getString(indexDate) + " " +
                        cursor.getString(indexCost) + " " +
                        cursor.getString(indexCategory));
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
                startActivity(new Intent(NewsActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.nav_costs_costs:
                break;
            case R.id.nav_costs_web:
                startActivity(new Intent(NewsActivity.this, WebActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }
        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.costs_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    protected void deleteNewsByTime() {
        /*DbHelper dbHelper = new DbHelper(NewsActivity.this);
        String sql = "DELETE FROM Costs WHERE DATE = date('now','-1 day')";
        dbHelper.getWritableDatabase().execSQL(sql);*/

        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String DateToStr = format.format(curDate);
        // System.out.println(DateToStr);
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from News", null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("ID_NEWS"));
            String dteToStr = cursor.getString(cursor.getColumnIndex("DATE"));

            Log.d("compareDates", DateToStr);
            Log.d("compareDates", dteToStr);

            if (DateToStr.equals(dteToStr)) {
                Toast.makeText(getApplicationContext(), "Даты совпадают", Toast.LENGTH_LONG).show();
            } else {
                database.delete("News", "ID_NEWS = ?", new String[]{Integer.toString(id)});
            }
        }

    }
    void exit()
    {
        System.exit(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_remove:
                deleteNewsByTime();
                return (true);
                case R.id.menu_exit:
              exit();
              return (true);
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

}