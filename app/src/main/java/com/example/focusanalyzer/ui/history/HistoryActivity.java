package com.example.focusanalyzer.ui.history;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.focusanalyzer.R;
import com.example.focusanalyzer.data.repository.SessionRepository;
import com.example.focusanalyzer.ui.insights.InsightsActivity;
import com.example.focusanalyzer.ui.timer.TimerActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SessionAdapter adapter = new SessionAdapter();
        recyclerView.setAdapter(adapter);

        SessionRepository repo = new SessionRepository(this);

        repo.getAllSessions().observe(this, adapter::setData);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_history);

        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_timer) {
                startActivity(new Intent(this, TimerActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }

            if (item.getItemId() == R.id.nav_insights) {
                startActivity(new Intent(this, InsightsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }

            return true;
        });
    }
}