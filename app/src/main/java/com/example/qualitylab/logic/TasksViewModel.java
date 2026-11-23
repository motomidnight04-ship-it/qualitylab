package com.example.qualitylab.logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qualitylab.data.TaskEntity;
import com.example.qualitylab.util.AppIdling;

import java.util.List;
import java.util.concurrent.Executor;

/** ViewModel экрана задач. */
public class TasksViewModel extends ViewModel {

    private final TaskRepository repository;
    private final Executor executor;
    private final MutableLiveData<List<TaskEntity>> tasks = new MutableLiveData<>();
    private final MutableLiveData<TaskValidator.TaskError> error = new MutableLiveData<>();

    public TasksViewModel(TaskRepository repository, Executor executor) {
        this.repository = repository;
        this.executor = executor;
    }

    public LiveData<List<TaskEntity>> getTasks() {
        return tasks;
    }

    public LiveData<TaskValidator.TaskError> getError() {
        return error;
    }

    public void load() {
        runInBackground(() -> tasks.postValue(repository.getAll()));
    }

    /** @return true, если задача принята и отправлена на сохранение */
    public boolean addTask(String title) {
        TaskValidator.TaskError result = TaskValidator.validate(title);
        error.setValue(result);
        if (result != TaskValidator.TaskError.NONE) {
            return false;
        }
        final String clean = title.trim();
        runInBackground(() -> {
            repository.add(clean);
            tasks.postValue(repository.getAll());
        });
        return true;
    }

    public void toggle(TaskEntity task) {
        final long id = task.id;
        final boolean newValue = !task.done;
        runInBackground(() -> {
            repository.setDone(id, newValue);
            tasks.postValue(repository.getAll());
        });
    }

    public void deleteDone() {
        runInBackground(() -> {
            repository.deleteDone();
            tasks.postValue(repository.getAll());
        });
    }

    public void seedDemo(int count) {
        runInBackground(() -> {
            repository.seedDemo(count);
            tasks.postValue(repository.getAll());
        });
    }

    private void runInBackground(Runnable work) {
        AppIdling.increment();
        executor.execute(() -> {
            try {
                work.run();
            } finally {
                AppIdling.decrement();
            }
        });
    }
}
