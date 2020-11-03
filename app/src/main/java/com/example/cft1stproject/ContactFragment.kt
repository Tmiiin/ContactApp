package com.example.cft1stproject

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.contact_fragment.*

class ContactFragment(private val mContext: Context) : Fragment() {

    companion object {
        fun newInstance(mContext: Context) = ContactFragment(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.contact_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contact_find.setOnClickListener { findContact() }
    }

    private fun findContact() {
        val input = contact_input_number.text.toString()
        val contactView: ContactView = getContactInfoByNumber(input)
        hideKeyboard()
        if (!contactView.isNullOrEmpty()) {
            contact_input_number.editableText.clear()
            
            childFragmentManager.beginTransaction().replace(
                R.id.contact_container,
                FoundContactFragment(contactView, input)
            )
                .commit()
        } else {
            childFragmentManager.beginTransaction().replace(
                R.id.contact_container,
                NotFoundContactFragment()
            )
                .commit()
        }
    }

    private fun hideKeyboard() {
        val imm =
            mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun getContactInfoByNumber(number: String?): ContactView {
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        var name = ""
        var image = "?"
        var finedNumber = "?"
        val contentResolver: ContentResolver = mContext.contentResolver
        val contactLookup = contentResolver.query(
            uri, arrayOf(
                BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.PHOTO_URI,
                ContactsContract.PhoneLookup.NUMBER
            ), null, null, null
        )
        contactLookup.use { contactLookup ->
            if (contactLookup != null && contactLookup.count > 0) {
                contactLookup.moveToNext()
                name =
                    contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                image =
                    contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.PHOTO_URI))
                finedNumber =
                    contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.PhoneLookup.NUMBER))
            }
        }
        return ContactView(name, image, finedNumber)
    }

}