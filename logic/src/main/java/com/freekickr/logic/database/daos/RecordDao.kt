package com.freekickr.logic.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.freekickr.logic.database.entities.RecordItem

@Dao
interface RecordDao {

    @Insert
    fun insert(record: RecordItem)

    @Update
    fun update(record: RecordItem)

    @Query("SELECT * FROM records_table WHERE id = :key")
    fun getRecord(key: Long?): RecordItem?

    @Query("DELETE FROM records_table")
    fun clearAll()

    @Query("DELETE FROM records_table WHERE id = :key")
    fun removeRecord(key: Long?)

    @Query("SELECT * FROM records_table ORDER BY id DESC")
    fun getAllRecords(): LiveData<MutableList<RecordItem>>

    @Query("SELECT COUNT(*) FROM records_table")
    fun getCount(): LiveData<Int>
}