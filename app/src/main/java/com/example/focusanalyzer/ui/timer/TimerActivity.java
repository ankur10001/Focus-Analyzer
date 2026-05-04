package com.example.focusanalyzer.ui.timer;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.focusanalyzer.R;
import com.example.focusanalyzer.data.local.SessionEntity;
import com.example.focusanalyzer.data.repository.SessionRepository;
import com.example.focusanalyzer.ui.history.HistoryActivity;
import com.example.focusanalyzer.ui.insights.InsightsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private long startTime;
    private boolean isRunning = false;
    private Handler handler = new Handler();
    private TextView timerText, totalSecondsText;
    private View pulseView, timerContainer;
    private ObjectAnimator pulseAnimator;

    private SessionRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Edge-to-Edge for premium feel and to fix overlap issues
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);

        repository = new SessionRepository(this);

        timerText = findViewById(R.id.timerText);
        // Fixed: Ensured clean reference to totalSecondsText ID
        totalSecondsText = findViewById(R.id.totalSecondsText);
        pulseView = findViewById(R.id.pulseView);
        timerContainer = findViewById(R.id.timerContainer);
        Spinner spinner = findViewById(R.id.categorySpinner);

        // Fix Overlap: Apply system bar insets to the root view
        View root = findViewById(R.id.main_root);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        String[] categories = {"Study", "Coding", "Reading", "Deep Work"};
        spinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                categories));

        Button startBtn = findViewById(R.id.startBtn);
        Button stopBtn = findViewById(R.id.stopBtn);

        setupAnimations();

        startBtn.setOnClickListener(v -> {
            if (!isRunning) {
                startTime = System.currentTimeMillis();
                isRunning = true;
                startPulseAnimation();
                runTimer();
                
                v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() -> 
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                ).start();
            }
        });

        stopBtn.setOnClickListener(v -> {
            if (isRunning) {
                Toast.makeText(this, "Session Saved ✅", Toast.LENGTH_SHORT).show();

                long endTime = System.currentTimeMillis();
                String category = spinner.getSelectedItem().toString();
                repository.insert(new SessionEntity(startTime, endTime, category));

                isRunning = false;
                stopPulseAnimation();
                handler.removeCallbacksAndMessages(null);
                
                // Visual reset
                timerText.setText("00:00:00");
                totalSecondsText.setText("0s");
            }
        });

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_timer);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_timer) return true;

            Intent intent = null;
            if (id == R.id.nav_history) {
                intent = new Intent(this, HistoryActivity.class);
            } else if (id == R.id.nav_insights) {
                intent = new Intent(this, InsightsActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
        
        if (root != null) {
            root.setAlpha(0f);
            root.animate().alpha(1f).setDuration(500).start();
        }
    }

    private void setupAnimations() {
        pulseAnimator = ObjectAnimator.ofPropertyValuesHolder(
                pulseView,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.4f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.4f),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0.6f, 0f)
        );
        pulseAnimator.setDuration(1500);
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private void startPulseAnimation() {
        pulseView.setVisibility(View.VISIBLE);
        pulseAnimator.start();
        timerContainer.animate().scaleX(1.05f).scaleY(1.05f).setDuration(300).start();
    }

    private void stopPulseAnimation() {
        pulseAnimator.cancel();
        pulseView.setVisibility(View.GONE);
        timerContainer.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start();
    }

    private void runTimer() {
        handler.post(new Runnable() {
            public void run() {
                if (isRunning) {
                    long elapsed = System.currentTimeMillis() - startTime;
                    int totalSec = (int) (elapsed / 1000);
                    
                    int hours = totalSec / 3600;
                    int minutes = (totalSec % 3600) / 60;
                    int secs = totalSec % 60;

                    timerText.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs));
                    if (totalSecondsText != null) {
                        totalSecondsText.setText(totalSec + "s");
                    }
                    
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }
}
