package com.example.savestateapp.data

import androidx.lifecycle.LiveData
import com.example.savestateapp.data.entity.Contact
import com.example.savestateapp.data.room.ContactDao

class ContactRepository(private val contactDao: ContactDao) : IContactRepository {
    override suspend fun insert(Contact: Contact) {
        contactDao.insert(Contact)
    }

    override suspend fun deleteAll() {
        contactDao.deleteAll()
    }

    override suspend fun deleteContact(contactId: Int) {
        contactDao.deleteContact(contactId)
    }

    override fun getAll(): LiveData<List<Contact>> {
        return contactDao.getAll()
    }

    override fun getContact(contactId: Int): Contact? {
        return contactDao.getContact(contactId)
    }

    override suspend fun updateContact(contactId: Int, displayName: String, number: String) {
        contactDao.updateContact(contactId, displayName, number)
    }

}