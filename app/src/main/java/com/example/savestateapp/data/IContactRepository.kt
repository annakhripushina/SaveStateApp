package com.example.savestateapp.data

import androidx.lifecycle.LiveData
import com.example.savestateapp.data.entity.Contact

interface IContactRepository {
    suspend fun insert(Contact: Contact)

    suspend fun deleteAll()

    suspend fun deleteContact(contactId: Int)

    fun getAll(): LiveData<List<Contact>>

    fun getContact(contactId: Int): Contact?

    suspend fun updateContact(contactId: Int, displayName: String, number: String)
}