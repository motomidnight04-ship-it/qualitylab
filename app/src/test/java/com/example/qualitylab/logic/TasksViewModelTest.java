package com.example.qualitylab.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.qualitylab.data.TaskEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

/** Задание 6: mock (Mockito) и fake для изоляции репозитория. */
public class TasksViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    /** Выполняет фоновые задачи сразу в том же потоке — тест детерминирован. */
    private final Executor direct = Runnable::run;

    private TaskRepository repository;
    private TasksViewModel viewModel;

    @Before
    public void setUp() {
        repository = mock(TaskRepository.class);
        viewModel = new TasksViewModel(repository, direct);
    }

    @Test
    public void addValidTask_savesTrimmedTitleAndPublishesList() {
        List<TaskEntity> stored = Arrays.asList(new TaskEntity("Купить хлеб", false));
        when(repository.getAll()).thenReturn(stored);

        boolean accepted = viewModel.addTask("  Купить хлеб ");

        assertTrue(accepted);
        verify(repository).add("Купить хлеб");
        assertEquals(stored, viewModel.getTasks().getValue());
        assertEquals(TaskValidator.TaskError.NONE, viewModel.getError().getValue());
    }

    @Test
    public void addEmptyTask_neverTouchesRepository() {
        boolean accepted = viewModel.addTask("   ");

        assertFalse(accepted);
        verifyNoInteractions(repository);
        assertEquals(TaskValidator.TaskError.EMPTY, viewModel.getError().getValue());
    }

    @Test
    public void addTooLongTask_isRejected() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append('x');
        }
        assertFalse(viewModel.addTask(sb.toString()));
        verify(repository, never()).add(org.mockito.ArgumentMatchers.anyString());
        assertEquals(TaskValidator.TaskError.TOO_LONG, viewModel.getError().getValue());
    }

    @Test
    public void toggle_flipsDoneFlag() {
        TaskEntity task = new TaskEntity("Тест", false);
        task.id = 5;

        viewModel.toggle(task);

        verify(repository).setDone(5, true);
    }

    @Test
    public void deleteDone_callsRepositoryAndRefreshes() {
        viewModel.deleteDone();

        verify(repository).deleteDone();
        verify(repository).getAll();
    }

    @Test
    public void load_publishesRepositoryContent() {
        List<TaskEntity> stored = Arrays.asList(new TaskEntity("A", true), new TaskEntity("B", false));
        when(repository.getAll()).thenReturn(stored);

        viewModel.load();

        assertEquals(2, viewModel.getTasks().getValue().size());
    }

    @Test
    public void seedDemo_asksRepositoryToSeed() {
        viewModel.seedDemo(50);

        verify(repository).seedDemo(50);
    }

    // ---- то же самое, но с FAKE вместо mock ----

    @Test
    public void withFakeRepository_newestTaskIsFirst() {
        TasksViewModel vm = new TasksViewModel(new FakeTaskRepository(), direct);

        vm.addTask("Первая");
        vm.addTask("Вторая");

        List<TaskEntity> result = vm.getTasks().getValue();
        assertEquals(2, result.size());
        assertEquals("Вторая", result.get(0).title);
    }

    @Test
    public void withFakeRepository_toggleAndDeleteDone() {
        TasksViewModel vm = new TasksViewModel(new FakeTaskRepository(), direct);
        vm.addTask("Сделать");
        vm.toggle(vm.getTasks().getValue().get(0));
        assertTrue(vm.getTasks().getValue().get(0).done);

        vm.deleteDone();

        assertEquals(0, vm.getTasks().getValue().size());
    }
}
