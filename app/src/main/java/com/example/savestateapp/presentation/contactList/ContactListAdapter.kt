package com.example.savestateapp.presentation.contactList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savestateapp.data.entity.Contact
import com.example.savestateapp.databinding.ContactItemBinding

class ContactListAdapter(
    private val listener: ContactClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listItem = ArrayList<Contact>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(contactList: ArrayList<Contact>) {
        listItem.clear()
        listItem.addAll(contactList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int =
        listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactListViewHolder -> {
                holder.bind(listItem[position], listener)
            }
        }
    }

    interface ContactClickListener {
        fun onEditClick(itemBinding: ContactItemBinding)
        fun onDeleteClick(contactItem: Contact)
        fun onSaveClick(
            itemBinding: ContactItemBinding,
            contactItem: Contact
        )
    }

}