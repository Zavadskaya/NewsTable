package com.example.user.task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.task.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BlockUsersActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference db;
    DatabaseReference users;

    ArrayList<String> usersIds = new ArrayList<>();
    ArrayList<User> usersList = new ArrayList<>();
    ArrayList<String> usernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_users);


        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_user);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance().getReference();
        users = db.child("users");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersIds.clear();
                usersList.clear();
                usernames.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    if (!ds.getKey().equals(auth.getCurrentUser().getUid()) || !u.getAdmin()) {
                        usersIds.add(ds.getKey());
                        usersList.add(u);
                        usernames.add(String.format("%s %s | %s", u.getFirstname(), u.getSecondname(), u.getBlocked() ? "Заблокирован" : "Не заблокирован"));
                    }
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    void initRecyclerView() {
        ListView r = findViewById(R.id.blockList);
        ArrayAdapter<String> rAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usernames);
        r.setAdapter(rAdapter);

        r.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = usersList.get(position);
                u.setBlocked(!u.getBlocked());
                users.child(usersIds.get(position)).setValue(u);


            }
        });
    }

    @SuppressWarnings("StatementWithEmp tyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_category_category:
                startActivity(new Intent(BlockUsersActivity.this, CategoryActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            case R.id.nav_costs_user:
                break;





        }
        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
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
                        BlockUsersActivity.super.onBackPressed();
                    }
                }).create().show();
    }






}
