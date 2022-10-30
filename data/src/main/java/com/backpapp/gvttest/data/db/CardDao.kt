package com.backpapp.gvttest.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {
    @Query("SELECT * FROM cardentity ORDER BY id")
    suspend fun getAll(): List<CardEntity>

    @Query("SELECT * FROM cardentity WHERE id = :cardId")
    suspend fun get(cardId: String): CardEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cards: CardEntity)

    @Query("DELETE FROM cardentity WHERE id = :cardId")
    suspend fun delete(cardId: String)
}