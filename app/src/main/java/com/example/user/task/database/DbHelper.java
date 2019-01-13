package com.example.user.task.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NewsDb.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table if not exists Category ( "
                + "NAME text primary key);");

        database.execSQL("create table if not exists User ( "
                + "keys text primary key,"
                + "firstname text not null,"
                + "secondname text not null,"
                + "course text not null,"
                + "groupe  text not null,"
                + "faculty text not null,"
                + "email text not null);");

        database.execSQL("create table if not exists News ( "
                + "ID_NEWS integer primary key autoincrement, "
                + "NEWS text not null, "
                + "DATE text not null, "
                + "CATEGORY text not null, "
                + "USERNAME text not null,"
                //+ "constraint FK_USER foreign key(USERNAME) references User(keys) on delete cascade on update cascade,"
                + "constraint FK_CATEGORY foreign key(CATEGORY) references Category(NAME) on delete cascade on update cascade);");

        database.execSQL("create index if not exists idx_сosts "
                + "on News(CATEGORY);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table Category;");
        database.execSQL("DROP TABLE News;");

        onCreate(database);
    }
}