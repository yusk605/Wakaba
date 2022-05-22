package com.example.wakaba.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WakabaDao {
    @Insert
        fun insert(bean:Wakaba)
    @Delete
        fun delete(bean:Wakaba)
    @Update
        fun update(bean: Wakaba)
    @Query("Select * from wakaba ORDER BY check_flag ASC,wakaba_rating desc;")
        fun getAll():LiveData<List<Wakaba>>
}