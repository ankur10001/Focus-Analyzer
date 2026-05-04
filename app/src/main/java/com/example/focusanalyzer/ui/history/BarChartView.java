package com.example.focusanalyzer.ui.history;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class BarChartView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Map<Integer, Long> data = new HashMap<>();
    private RectF rectF = new RectF();

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        axisPaint.setColor(Color.parseColor("#4A5568"));
        axisPaint.setStrokeWidth(2f);
    }

    public void setData(Map<Integer, Long> data) {
        this.data = data != null ? data : new HashMap<>();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int paddingBottom = 40; // Space for labels
        int chartHeight = height - paddingBottom;

        if (data.isEmpty()) {
            paint.setColor(Color.parseColor("#4A5568"));
            paint.setTextSize(30f);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("No data available", width / 2f, chartHeight / 2f, paint);
            return;
        }

        float barWidth = (float) width / 24;
        long max = 0;
        for (long v : data.values()) {
            if (v > max) max = v;
        }

        // Draw horizontal baseline
        canvas.drawLine(0, chartHeight, width, chartHeight, axisPaint);

        for (int hour = 0; hour < 24; hour++) {
            long value = data.getOrDefault(hour, 0L);
            float ratio = max == 0 ? 0 : (float) value / max;
            float barHeight = chartHeight * ratio * 0.8f; // Max 80% height

            float left = hour * barWidth + 4;
            float top = chartHeight - barHeight;
            float right = (hour + 1) * barWidth - 4;
            float bottom = chartHeight;

            rectF.set(left, top, right, bottom);

            // Premium Gradient: Indigo to Blue
            paint.setShader(new LinearGradient(0, top, 0, bottom,
                    Color.parseColor("#6366F1"), Color.parseColor("#3B82F6"),
                    Shader.TileMode.CLAMP));

            // Rounded corners for a modern look
            canvas.drawRoundRect(rectF, 12, 12, paint);
            paint.setShader(null);

            // Simple labels for every 6 hours
            if (hour % 6 == 0) {
                paint.setColor(Color.parseColor("#718096"));
                paint.setTextSize(24f);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(hour + "h", (left + right) / 2f, height - 10, paint);
            }
        }
    }
}
