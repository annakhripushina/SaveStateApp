package com.example.savestateapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.savestateapp.data.entity.Contact

@Database(entities = [Contact::class], version = 1)
abstract class ContactDataBase : RoomDatabase() {

    abstract val contactDao: ContactDao

    companion object {
        private var instance: ContactDataBase? = null

        fun getDatabase(context: Context): ContactDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDataBase::class.java,
                    "contactList-db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as ContactDataBase
        }
    }
}