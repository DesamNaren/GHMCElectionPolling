package com.cgg.ghmcpollingapp.room.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cgg.ghmcpollingapp.room.dao.PollingMasterDao;
import com.cgg.ghmcpollingapp.source.PollingEntity;

/*
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */

@Database(entities = {PollingEntity.class},
        version = 1, exportSchema = false)
public abstract class PollingDatabase extends RoomDatabase {

    private static PollingDatabase INSTANCE;

    public abstract PollingMasterDao pollingDao();

    public static PollingDatabase getDatabase(final Context context) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PollingDatabase.class, "polling.db")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
//                            .createFromFile(new File("database/districts.json"))
                            .createFromAsset("database/polling.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static Callback sRoomDatabaseCallback = new Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
