package com.example.qualitylab.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insert(TaskEntity task);

    @Update
    int update(TaskEntity task);

    @Delete
    int delete(TaskEntity task);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<TaskEntity> getAll();

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity getById(long id);

    @Query("SELECT COUNT(*) FROM tasks")
    int count();

    @Query("DELETE FROM tasks WHERE done = 1")
    int deleteDone();
}
