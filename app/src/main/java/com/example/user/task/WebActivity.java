package com.example.user.task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WebActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener{

    public ArrayList<String> titleList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView mListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);


        Toolbar toolbar = findViewById(R.id.toolbar_web);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.web_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_web);
        navigationView.setNavigationItemSelectedListener(this);

        mListView = findViewById(R.id.list_view);


        titleList.clear();
        new WebTask().execute("http://www.belstu.by/news.html?list=20&filter=1", ".TitleRef");
        adapter = new ArrayAdapter<>(this, R.layout.list_view, R.id.text_item, titleList);
    }

    public class WebTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg) {
            try
            {
                Document doc = Jsoup.connect(arg[0]).get();
                Elements titleContent = doc.select(arg[1]);

                for (Element contents: titleContent) {
                    titleList.add(contents.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String results) {
            mListView.setAdapter(adapter);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

                case R.id.nav_home:
                    startActivity(new Intent(WebActivity.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.nav_costs_web:
                    break;
                case R.id.nav_costs_costs:
                    startActivity(new Intent(WebActivity.this, NewsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;


        }
        DrawerLayout drawer = findViewById(R.id.web_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        WebActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
