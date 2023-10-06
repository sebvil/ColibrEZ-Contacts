package com.colibrez.contacts.android

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.colibrez.contacts.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AndroidContact(val name: String, val lookupKey: String)

class MainViewModel(private val contentResolver: ContentResolver) : ViewModel() {

    private val _state: MutableStateFlow<List<AndroidContact>> = MutableStateFlow(listOf())
    val state: StateFlow<List<AndroidContact>>
        get() = _state

    fun fetchContact(lookupKey: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Sets the columns to retrieve for the user profile
                val projection: Array<out String> = arrayOf(
                    ContactsContract.CommonDataKinds.Event.TYPE,
                    ContactsContract.CommonDataKinds.Event.START_DATE
                )


                val selection =
                    "${ContactsContract.Data.LOOKUP_KEY} = ? AND " +
                            "${ContactsContract.Data.MIMETYPE} = '${ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE}'"


                // Retrieves the profile from the Contacts Provider
                val profileCursor = contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    projection,
                    selection,
                    arrayOf(lookupKey),
                    null
                )

                val contacts = mutableListOf<Contact>()
                if (profileCursor != null && profileCursor.moveToNext()) {
                    val indexes = projection.map { Pair(it, profileCursor.getColumnIndex(it)) }
                    do {
                        indexes.forEach {
                            Log.i("C", "${it.first}: ${profileCursor.getString(it.second)}")
                        }
                    } while (profileCursor.moveToNext())

                }

                profileCursor?.close()
            }
        }
    }

    fun fetchContacts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Sets the columns to retrieve for the user profile
                val projection = arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.LOOKUP_KEY
                )

                // Retrieves the profile from the Contacts Provider
                val profileCursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                )

                val contacts = mutableListOf<AndroidContact>()
                if (profileCursor != null && profileCursor.moveToNext()) {
                    val nameIndex =
                        profileCursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME_PRIMARY)
                    val lookupKeyIndex =
                        profileCursor.getColumnIndex(ContactsContract.Profile.LOOKUP_KEY)
                    do {
                        val name = profileCursor.getString(nameIndex)
                        val lookupKey = profileCursor.getString(lookupKeyIndex)
                        contacts.add(AndroidContact(name, lookupKey))
                    } while (profileCursor.moveToNext())

                    _state.update {
                        contacts
                    }
                }

                profileCursor?.close()
            }
        }
    }

    class Factory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(contentResolver) as T
        }
    }
}