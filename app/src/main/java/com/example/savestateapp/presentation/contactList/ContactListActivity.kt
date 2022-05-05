package com.example.savestateapp.presentation.contactList

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.savestateapp.R
import com.example.savestateapp.data.entity.Contact
import com.example.savestateapp.databinding.ContactItemBinding
import com.example.savestateapp.databinding.ContactListFragmentBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ContactListActivity : Fragment() {
    companion object {
        const val APP_PREFERENCES = "mySettings"
        const val APP_PREFERENCES_FIRST_RUN = "firstRun"
        const val FILE_NAME = "contactList.json"
    }

    var prefsFirstRun: Boolean = true

    private lateinit var binding: ContactListFragmentBinding
    private val viewModel: ContactListViewModel by viewModels()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var task: Future<Any>? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                requestContacts()
            }
        }

    private lateinit var recyclerView: RecyclerView

    private val adapter = ContactListAdapter(object : ContactListAdapter.ContactClickListener {
        override fun onEditClick(itemBinding: ContactItemBinding) {
            setButtonProperties(itemBinding, true, VISIBLE, INVISIBLE)
        }

        override fun onDeleteClick(contactItem: Contact) {
            contactItem.id?.let { viewModel.deleteContact(it) }
        }

        override fun onSaveClick(itemBinding: ContactItemBinding, contactItem: Contact) {
            contactItem.id?.let {
                viewModel.updateContact(
                    it,
                    itemBinding.displayName.text.toString(),
                    itemBinding.number.text.toString()
                )
            }
            setButtonProperties(itemBinding, false, INVISIBLE, VISIBLE)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefsFirstRun = requireContext().getSharedPreferences(
            APP_PREFERENCES,
            Context.MODE_PRIVATE
        ).getBoolean(APP_PREFERENCES_FIRST_RUN, true)

        binding = ContactListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initRecycler()

        viewModel.contactList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter.setItems(list as ArrayList<Contact>)
            }
        })

        onClickButton()
    }

    override fun onResume() {
        super.onResume()
        if (prefsFirstRun == true) {
            requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(APP_PREFERENCES_FIRST_RUN, false)
                .apply()
        }
    }

    private fun initRecycler() {
        recyclerView = binding.contactList
        recyclerView.adapter = adapter
    }

    private fun checkPermission() {
        if (prefsFirstRun)
            if (isPermissionGranted()) {
                requestContacts()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
    }

    private fun isPermissionGranted(): Boolean =
        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        )

    private fun requestContacts() {
        task = executor.submit<Any> {
            try {
                viewModel.retrieveContacts()
            } catch (error: NullPointerException) {
                error.message
            }
        }
    }

    private fun onClickButton() {
        binding.buttonAdd.setOnClickListener {
            viewModel.insertContact(
                Contact(
                    getString(R.string.noNameText),
                    getString(R.string.noNunberText)
                )
            )
        }
        binding.buttonDeleteAll.setOnClickListener {
            viewModel.deleteAll()
        }
        binding.buttonUpdate.setOnClickListener {
            requestContacts()
        }
        binding.buttonUFile.setOnClickListener {
            viewModel.createFileIfNotExist(requireActivity().application, FILE_NAME)
            viewModel.saveFile(requireActivity().application)
        }

    }

    private fun setButtonProperties(
        binding: ContactItemBinding,
        isEnabled: Boolean,
        visibilitySave: Int,
        visibilityEdit: Int
    ) {
        binding.buttonSave.visibility = visibilitySave
        binding.buttonEdit.visibility = visibilityEdit
        binding.displayName.isEnabled = isEnabled
        binding.number.isEnabled = isEnabled
    }


}