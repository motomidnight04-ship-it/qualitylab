package com.example.qualitylab.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/** Задание 8: тест DAO на in-memory базе. */
@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private AppDatabase db;
    private TaskDao dao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        dao = db.taskDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndRead() {
        long id = dao.insert(new TaskEntity("Купить молоко", false));

        TaskEntity loaded = dao.getById(id);
        assertEquals("Купить молоко", loaded.title);
        assertFalse(loaded.done);
        assertEquals(0, loaded.priority);
    }

    @Test
    public void getAll_returnsNewestFirst() {
        dao.insert(new TaskEntity("Первая", false));
        dao.insert(new TaskEntity("Вторая", false));

        List<TaskEntity> all = dao.getAll();

        assertEquals(2, all.size());
        assertEquals("Вторая", all.get(0).title);
    }

    @Test
    public void update_changesDoneFlag() {
        long id = dao.insert(new TaskEntity("Задача", false));
        TaskEntity task = dao.getById(id);
        task.done = true;

        dao.update(task);

        assertTrue(dao.getById(id).done);
    }

    @Test
    public void delete_removesRow() {
        long id = dao.insert(new TaskEntity("Удалить", false));

        dao.delete(dao.getById(id));

        assertNull(dao.getById(id));
        assertEquals(0, dao.count());
    }

    @Test
    public void deleteDone_removesOnlyCompleted() {
        dao.insert(new TaskEntity("Готово 1", true));
        dao.insert(new TaskEntity("Готово 2", true));
        dao.insert(new TaskEntity("В работе", false));

        int removed = dao.deleteDone();

        assertEquals(2, removed);
        assertEquals(1, dao.count());
    }
}
