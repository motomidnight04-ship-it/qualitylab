package com.example.qualitylab.logic;

import com.example.qualitylab.data.TaskEntity;

import java.util.List;

/**
 * Хранилище задач. Все методы блокирующие — вызывать только из фонового потока
 * (это делает TasksViewModel).
 */
public interface TaskRepository {

    List<TaskEntity> getAll();

    long add(String title);

    void setDone(long id, boolean done);

    int deleteDone();

    void seedDemo(int count);
}
