package com.kiranstore.manager.services.contacts

import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class PhoneContact(
    val name: String,
    val phone: String
)

@Singleton
class ContactsService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getAllContacts(): List<PhoneContact> {
        val contacts = mutableListOf<PhoneContact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor = context.contentResolver.query(
            uri, projection, null, null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )
        cursor?.use {
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val name = it.getString(nameIdx) ?: continue
                val phone = it.getString(phoneIdx) ?: continue
                contacts.add(PhoneContact(name.trim(), phone.trim()))
            }
        }
        return contacts.distinctBy { it.phone }
    }

    fun searchContacts(query: String): List<PhoneContact> =
        getAllContacts().filter {
            it.name.contains(query, ignoreCase = true) ||
            it.phone.contains(query)
        }
}
