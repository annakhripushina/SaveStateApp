package com.example.savestateapp.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contactTable",
    indices = [Index(value = ["displayName", "number"], unique = true)]
)
data class Contact(
    val displayName: String,
    val number: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}