package com.example.qualitylab.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.qualitylab.R;
import com.example.qualitylab.data.TaskEntity;
import com.example.qualitylab.logic.TaskValidator;
import com.example.qualitylab.logic.TasksViewModel;
import com.example.qualitylab.util.AppLog;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "TasksActivity";

    private TasksViewModel viewModel;
    private final List<TaskEntity> current = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private EditText etTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        etTitle = findViewById(R.id.et_task_title);
        ListView list = findViewById(R.id.list_tasks);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        list.setAdapter(adapter);

        viewModel = new ViewModelProvider(this, new TasksViewModelFactory(this)).get(TasksViewModel.class);

        viewModel.getTasks().observe(this, tasks -> {
            AppLog.d(TAG, "Список задач обновлён: " + tasks.size());
            current.clear();
            current.addAll(tasks);
            List<String> labels = new ArrayList<>();
            for (TaskEntity t : tasks) {
                labels.add((t.done ? "✔ " : "○ ") + t.title);
            }
            adapter.clear();
            adapter.addAll(labels);
        });

        viewModel.getError().observe(this, error -> {
            if (error == TaskValidator.TaskError.EMPTY) {
                etTitle.setError(getString(R.string.err_task_empty));
            } else if (error == TaskValidator.TaskError.TOO_LONG) {
                etTitle.setError(getString(R.string.err_task_too_long));
            } else {
                etTitle.setError(null);
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(v -> {
            if (viewModel.addTask(etTitle.getText().toString())) {
                etTitle.setText("");
            }
        });
        findViewById(R.id.btn_seed).setOnClickListener(v -> viewModel.seedDemo(50));
        findViewById(R.id.btn_clear_done).setOnClickListener(v -> viewModel.deleteDone());
        list.setOnItemClickListener((parent, view, position, id) -> {
            if (position < current.size()) {
                viewModel.toggle(current.get(position));
            }
        });

        viewModel.load();
    }
}
