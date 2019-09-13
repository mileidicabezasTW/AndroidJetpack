package com.raywenderlich.android.imet.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.raywenderlich.android.imet.data.model.People

@Dao
interface PeopleDao {
    //1: Select all
    @Query("SELECT * FROM People ORDER BY id DESC")
    fun getAll() : List <People>

    //2: Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(people: People)

    //3: Delete
    @Query("DELETE FROM People")
    fun deleteAll()

    // 4: Select by id
    @Query("SELECT * FROM People WHERE id = :id")
    fun find(id: Int): People

    fun getAllPeople () : LiveData <List <People>>

}