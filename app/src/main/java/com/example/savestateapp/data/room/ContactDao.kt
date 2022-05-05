package com.example.savestateapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.savestateapp.data.entity.Contact


@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(Contact: Contact)

    @Query("DELETE FROM contactTable")
    fun deleteAll()

    @Query("DELETE FROM contactTable WHERE id = :contactId ")
    fun deleteContact(contactId: Int)

    @Query("SELECT * FROM contactTable")
    fun getAll(): LiveData<List<Contact>>

    @Query("SELECT * FROM contactTable WHERE id = :contactId")
    fun getContact(contactId: Int): Contact?

    @Query("UPDATE contactTable SET displayName = :displayName, number = :number WHERE id = :contactId")
    fun updateContact(contactId: Int, displayName: String, number: String)

}