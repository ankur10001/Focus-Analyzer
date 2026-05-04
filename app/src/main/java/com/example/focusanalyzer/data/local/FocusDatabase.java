package com.example.focusanalyzer.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SessionEntity.class}, version = 1)
public abstract class FocusDatabase extends RoomDatabase {

    private static FocusDatabase instance;

    public abstract SessionDao sessionDao();

    public static synchronized FocusDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    FocusDatabase.class,
                    "focus_db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}