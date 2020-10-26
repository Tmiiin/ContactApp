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
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment


class ContactFragment(private val mContext: Context) : Fragment() {

    lateinit var contactContainer: FrameLayout
    lateinit var contactEditNumber: EditText
    lateinit var findButton: Button

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
        findButton.setOnClickListener { findContact() }
    }

    private fun findContact() {
        var name: String? = getContactDisplayNameByNumber(contactEditNumber.text.toString())
        val imm =
            mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        if (!name.isNullOrEmpty()) {
            childFragmentManager.beginTransaction().replace(
                R.id.contact_container,
                FoundContactFragment(name, contactEditNumber.text.toString())
            )
                .commit()
        } else {
            contactEditNumber.editableText.clear()
            childFragmentManager.beginTransaction().replace(
                R.id.contact_container,
                NotFoundContactFragment()
            )

        }
    }

    private fun getContactDisplayNameByNumber(number: String?): String? {

        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        var name = "?"
        val contentResolver: ContentResolver = mContext.contentResolver
        val contactLookup = contentResolver.query(
            uri, arrayOf(
                BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME
            ), null, null, null
        )
        try {
            if (contactLookup != null && contactLookup.count > 0) {
                contactLookup.moveToNext()
                name =
                    contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } finally {
            contactLookup?.close()
        }
        return name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contactEditNumber = view.findViewById(R.id.contact_input_number)
        contactContainer = view.findViewById(R.id.contact_container)
        findButton = view.findViewById(R.id.contact_find)
        super.onViewCreated(view, savedInstanceState)
    }

}