package com.example.savestateapp.domain

import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.savestateapp.data.entity.Contact

class ContactInteractor : IContactInteractor {

    override fun retrieveContacts(contentResolver: ContentResolver): List<Contact> {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        val contacts = mutableListOf<Contact>()

        cursor?.use {
            val indexName =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val name = cursor.getString(indexName)
                val number = cursor.getString(indexNumber)

                contacts.add(Contact(name, number))

            }
        }
        return contacts
    }
}