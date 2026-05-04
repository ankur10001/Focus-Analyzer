package com.example.focusanalyzer.ui.insights;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.focusanalyzer.R;
import com.example.focusanalyzer.data.local.SessionEntity;
import com.example.focusanalyzer.data.repository.SessionRepository;
import com.example.focusanalyzer.ui.history.BarChartView;
import com.example.focusanalyzer.ui.history.HistoryActivity;
import com.example.focusanalyzer.ui.timer.TimerActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsightsActivity extends AppCompatActivity {

    private SessionRepository repo;
    private TextView scoreText, streakText, goalText, insightText;
    private BarChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        repo = new SessionRepository(this);

        scoreText = findViewById(R.id.scoreText);
        streakText = findViewById(R.id.streakText);
        goalText = findViewById(R.id.goalText);
        insightText = findViewById(R.id.insightText);
        chart = findViewById(R.id.chart);

        setupBottomNav();
        observeData();
    }

    private void observeData() {
        repo.getAllSessions().observe(this, sessions -> {
            if (sessions == null || sessions.isEmpty()) {
                updateEmptyState();
                return;
            }
            calculateStats(sessions);
            updateChart(sessions);
        });
    }

    private void updateEmptyState() {
        scoreText.setText("0");
        streakText.setText("0 Days");
        goalText.setText("0%");
        insightText.setText("Start your first session to see insights!");
        chart.setData(new HashMap<>());
    }

    private void calculateStats(List<SessionEntity> sessions) {
        long todayMillis = 0;
        long now = System.currentTimeMillis();
        
        // Start of today (midnight)
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        long startOfToday = todayCal.getTimeInMillis();

        Map<Integer, Long> hourlyGlobalData = new HashMap<>();
        Calendar cal = Calendar.getInstance();

        for (SessionEntity s : sessions) {
            long duration = s.getEndTime() - s.getStartTime();

            if (s.getStartTime() >= startOfToday) {
                todayMillis += duration;
            }

            // Calculate Peak Hour across all history
            cal.setTimeInMillis(s.getStartTime());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            hourlyGlobalData.put(hour, hourlyGlobalData.getOrDefault(hour, 0L) + duration);
        }

        // REFACTORED FOR SECONDS PRECISION
        long todaySeconds = todayMillis / 1000;
        
        // Productivity Score based on today (Goal: 14,400 seconds = 4 hours)
        int score = (int) Math.min(100, (todaySeconds * 100) / 14400);
        scoreText.setText(String.valueOf(score));

        // Find Peak Hour
        int peakHour = -1;
        long maxDuration = -1;
        for (Map.Entry<Integer, Long> entry : hourlyGlobalData.entrySet()) {
            if (entry.getValue() > maxDuration) {
                maxDuration = entry.getValue();
                peakHour = entry.getKey();
            }
        }

        String peakTimeStr = peakHour != -1 ? formatHour(peakHour) : "--";
        goalText.setText(peakTimeStr); 

        streakText.setText("Active");

        // Advanced Insights using high-precision seconds
        if (peakHour != -1) {
            insightText.setText("Your Focus Prime-Time is around " + peakTimeStr + 
                ". You've spent " + todaySeconds + " seconds focused today. Focus harder during your peak window for maximum efficiency.");
        } else {
            insightText.setText("Keep tracking your sessions. We need more data to find your peak focus hours.");
        }
    }

    private String formatHour(int hour) {
        if (hour == 0) return "12 AM";
        if (hour < 12) return hour + " AM";
        if (hour == 12) return "12 PM";
        return (hour - 12) + " PM";
    }

    private void updateChart(List<SessionEntity> sessions) {
        Map<Integer, Long> todayHourlyData = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        long startOfToday = todayCal.getTimeInMillis();

        for (SessionEntity s : sessions) {
            if (s.getStartTime() >= startOfToday) {
                cal.setTimeInMillis(s.getStartTime());
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                long duration = s.getEndTime() - s.getStartTime();
                todayHourlyData.put(hour, todayHourlyData.getOrDefault(hour, 0L) + duration);
            }
        }
        chart.setData(todayHourlyData);
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_insights);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_timer) {
                startActivity(new Intent(this, TimerActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            if (id == R.id.nav_history) {
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            return true;
        });
    }
}
