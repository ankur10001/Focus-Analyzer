package com.example.focusanalyzer.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sessions")
public class SessionEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long startTime;
    private long endTime;
    private String category;

    public SessionEntity(long startTime, long endTime, String category) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
