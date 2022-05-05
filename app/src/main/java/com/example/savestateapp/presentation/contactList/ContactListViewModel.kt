package com.example.savestateapp.presentation.contactList

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.savestateapp.data.ContactRepository
import com.example.savestateapp.data.IContactRepository
import com.example.savestateapp.data.entity.Contact
import com.example.savestateapp.data.room.ContactDataBase
import com.example.savestateapp.domain.ContactInteractor
import com.example.savestateapp.domain.IContactInteractor
import com.example.savestateapp.presentation.contactList.ContactListActivity.Companion.FILE_NAME
import com.example.savestateapp.util.SingleLiveEvent
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import java.io.File


class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private var contactRepository: IContactRepository
    private var contactInteractor: IContactInteractor
    private var contentResolver: ContentResolver
    private var _contactList: MutableLiveData<List<Contact>> = SingleLiveEvent()

    var contactList: LiveData<List<Contact>> = _contactList

    init {
        val contactDao = ContactDataBase.getDatabase(application).contactDao
        contactRepository = ContactRepository(contactDao)
        contactInteractor = ContactInteractor()
        contentResolver = application.contentResolver
        contactList = contactRepository.getAll()
    }

    fun retrieveContacts() {
        val contacts = contactInteractor.retrieveContacts(contentResolver)
        contacts.forEach {
            viewModelScope.launch {
                contactRepository.insert(it)
            }
        }
        _contactList.value = contacts
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.insert(contact)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            contactRepository.deleteAll()
        }
    }

    fun deleteContact(contactId: Int) {
        viewModelScope.launch {
            contactRepository.deleteContact(contactId)
        }
    }

    fun updateContact(id: Int, displayName: String, number: String) {
        viewModelScope.launch {
            contactRepository.updateContact(
                id,
                displayName,
                number
            )
        }
    }

    fun createFileIfNotExist(application: Application, pathName: String) {
        val folder = File("${application.filesDir}")
        val file = File(folder.absolutePath + "/$pathName")
        if (!folder.exists()) {
            folder.mkdir()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    fun saveFile(application: Application) {
        application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { fileOutputStream ->
            val gson = GsonBuilder().create()
            fileOutputStream.write(gson.toJson(contactList.value).toByteArray())
        }
    }

}