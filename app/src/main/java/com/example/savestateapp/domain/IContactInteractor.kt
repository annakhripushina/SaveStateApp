package com.example.savestateapp.domain

import android.content.ContentResolver
import com.example.savestateapp.data.entity.Contact

interface IContactInteractor {
    fun retrieveContacts(contentResolver: ContentResolver): List<Contact>
}