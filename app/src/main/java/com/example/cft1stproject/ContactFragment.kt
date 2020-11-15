package com.example.cft1stproject

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.cft1stproject.model.ContactView
import com.example.cft1stproject.retrofit.ImgurAPI
import kotlinx.android.synthetic.main.contact_fragment.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ContactFragment(private val mContext: Context) : Fragment(), OnBackPressedListener {

    lateinit var contactView: ContactView

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
        download_to_imgur.setOnClickListener {
            replaceToDownloadFragment(contactView)
        }
    }

    fun replaceToDownloadFragment(contactView: ContactView) {
        childFragmentManager.beginTransaction().replace(
            R.id.contact_container, DownloadFragment( mContext, contactView)
        )
            .addToBackStack("DownloadFragment")
            .commit()
    }

    private fun findContact() {
        val input = contact_input_number.text.toString()
        contactView = getContactInfoByNumber(input)
        hideKeyboard()
        if (!contactView.isNullOrEmpty()) {
            contact_input_number.editableText.clear()
            download_to_imgur.visibility = View.VISIBLE
            childFragmentManager.beginTransaction().replace(
                R.id.contact_container,
                FoundContactFragment(contactView)
            )
                .commit()
        } else {
            download_to_imgur.visibility = View.GONE
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
                image = try {
                    contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.PHOTO_URI))
                } catch (e: Exception) {
                    "android.resource://com.example.cft1stproject/" + R.drawable.unknown_person
                }
                finedNumber =
                    contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.PhoneLookup.NUMBER))
            }
        }
        return ContactView(name, image, finedNumber)
    }

    override fun onBackPressed() {
        childFragmentManager.popBackStack()
    }

}