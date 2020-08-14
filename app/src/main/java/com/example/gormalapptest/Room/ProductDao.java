package com.example.gormalapptest.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM ProductEntity")
    List<ProductEntity> getAll();

    @Insert
    void insert(ProductEntity productEntity);
}