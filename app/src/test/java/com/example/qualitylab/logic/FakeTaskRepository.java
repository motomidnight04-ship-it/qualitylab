package com.example.qualitylab.logic;

import com.example.qualitylab.data.TaskEntity;

import java.util.ArrayList;
import java.util.List;

/** FAKE: настоящая рабочая реализация, но в памяти (Задание 6). */
public class FakeTaskRepository implements TaskRepository {

    private final List<TaskEntity> items = new ArrayList<>();
    private long nextId = 1;

    @Override
    public List<TaskEntity> getAll() {
        List<TaskEntity> copy = new ArrayList<>(items);
        java.util.Collections.reverse(copy); // новые сверху, как в базе
        return copy;
    }

    @Override
    public long add(String title) {
        TaskEntity t = new TaskEntity(title, false);
        t.id = nextId++;
        items.add(t);
        return t.id;
    }

    @Override
    public void setDone(long id, boolean done) {
        for (TaskEntity t : items) {
            if (t.id == id) {
                t.done = done;
            }
        }
    }

    @Override
    public int deleteDone() {
        int before = items.size();
        items.removeIf(t -> t.done);
        return before - items.size();
    }

    @Override
    public void seedDemo(int count) {
        for (int i = 1; i <= count; i++) {
            add("Демо " + i);
        }
    }
}
