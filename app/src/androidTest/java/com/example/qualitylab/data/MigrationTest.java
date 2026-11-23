package com.example.qualitylab.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/** Задание 8: миграция базы 1 → 2 сохраняет старые данные. */
@RunWith(AndroidJUnit4.class)
public class MigrationTest {

    private static final String DB_NAME = "migration-test.db";
    private final Context context = ApplicationProvider.getApplicationContext();

    @Before
    @After
    public void cleanUp() {
        context.deleteDatabase(DB_NAME);
    }

    @Test
    public void migrate1To2_keepsDataAndAddsPriority() {
        // База версии 1 (без колонки priority), созданная вручную
        SQLiteDatabase old = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        old.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "title TEXT NOT NULL, done INTEGER NOT NULL)");
        old.execSQL("INSERT INTO tasks (title, done) VALUES ('Старая задача', 1)");
        old.setVersion(1);
        old.close();

        AppDatabase migrated = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .allowMainThreadQueries()
                .build();
        try {
            List<TaskEntity> all = migrated.taskDao().getAll();
            assertEquals(1, all.size());
            assertEquals("Старая задача", all.get(0).title);
            assertTrue(all.get(0).done);
            assertEquals(0, all.get(0).priority);
        } finally {
            migrated.close();
        }
    }
}
