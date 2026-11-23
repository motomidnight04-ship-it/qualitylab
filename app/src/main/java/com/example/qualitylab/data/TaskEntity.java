package com.example.qualitylab.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String title;

    public boolean done;

    /** Появилось в версии 2 базы данных (миграция 1 → 2). */
    @ColumnInfo(defaultValue = "0")
    public int priority;

    public TaskEntity(@NonNull String title, boolean done) {
        this.title = title;
        this.done = done;
    }
}
