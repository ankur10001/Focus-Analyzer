package com.example.focusanalyzer.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.focusanalyzer.data.local.FocusDatabase;
import com.example.focusanalyzer.data.local.SessionDao;
import com.example.focusanalyzer.data.local.SessionEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionRepository {

    private SessionDao dao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public SessionRepository(Context context) {
        dao = FocusDatabase.getInstance(context).sessionDao();
    }

    public void insert(SessionEntity session) {
        executor.execute(() -> dao.insert(session));
    }

    public LiveData<List<SessionEntity>> getAllSessions() {
        return dao.getAllSessions();
    }

    // 🔥 NEW (today sessions)
    public LiveData<List<SessionEntity>> getTodaySessions() {
        long now = System.currentTimeMillis();
        long startOfDay = now - (now % (24 * 60 * 60 * 1000));
        return dao.getSessionsFrom(startOfDay);
    }
}