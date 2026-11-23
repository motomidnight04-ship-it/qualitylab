package com.example.qualitylab.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.qualitylab.data.AppDatabase;
import com.example.qualitylab.data.RoomTaskRepository;
import com.example.qualitylab.logic.TasksViewModel;
import com.example.qualitylab.util.AppExecutors;

public class TasksViewModelFactory implements ViewModelProvider.Factory {

    private final Context appContext;

    public TasksViewModelFactory(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        RoomTaskRepository repository = new RoomTaskRepository(AppDatabase.get(appContext).taskDao());
        return (T) new TasksViewModel(repository, AppExecutors.io());
    }
}
