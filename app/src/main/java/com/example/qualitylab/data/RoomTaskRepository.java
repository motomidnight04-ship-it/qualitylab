package com.example.qualitylab.data;

import com.example.qualitylab.logic.TaskRepository;

import java.util.List;

public class RoomTaskRepository implements TaskRepository {

    private final TaskDao dao;

    public RoomTaskRepository(TaskDao dao) {
        this.dao = dao;
    }

    @Override
    public List<TaskEntity> getAll() {
        return dao.getAll();
    }

    @Override
    public long add(String title) {
        return dao.insert(new TaskEntity(title, false));
    }

    @Override
    public void setDone(long id, boolean done) {
        TaskEntity task = dao.getById(id);
        if (task != null) {
            task.done = done;
            dao.update(task);
        }
    }

    @Override
    public int deleteDone() {
        return dao.deleteDone();
    }

    @Override
    public void seedDemo(int count) {
        for (int i = 1; i <= count; i++) {
            dao.insert(new TaskEntity("Демо-задача №" + i, i % 3 == 0));
        }
    }
}
