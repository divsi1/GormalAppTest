package com.example.gormalapptest.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ProductEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}