package com.example.savestateapp.presentation.contactList

import androidx.recyclerview.widget.RecyclerView
import com.example.savestateapp.data.entity.Contact
import com.example.savestateapp.databinding.ContactItemBinding


class ContactListViewHolder(private val itemBinding: ContactItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(contactItem: Contact, listener: ContactListAdapter.ContactClickListener) {
        itemBinding.displayName.setText(contactItem.displayName)
        itemBinding.number.setText(contactItem.number)
        itemBinding.displayName.isEnabled = false
        itemBinding.number.isEnabled = false

        itemBinding.buttonDelete.setOnClickListener {
            listener.onDeleteClick(
                contactItem
            )
        }

        itemBinding.buttonEdit.setOnClickListener {
            listener.onEditClick(
                itemBinding
            )
        }

        itemBinding.buttonSave.setOnClickListener {
            listener.onSaveClick(
                itemBinding,
                contactItem
            )
        }

    }
}