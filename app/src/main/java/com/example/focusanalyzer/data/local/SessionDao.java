package com.example.focusanalyzer.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SessionDao {

    @Insert
    void insert(SessionEntity session);

    // ✅ FIXED: correct table name
    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    LiveData<List<SessionEntity>> getAllSessions();

    // ✅ for streak / today data
    @Query("SELECT * FROM sessions WHERE startTime >= :startOfDay")
    LiveData<List<SessionEntity>> getSessionsFrom(long startOfDay);
}