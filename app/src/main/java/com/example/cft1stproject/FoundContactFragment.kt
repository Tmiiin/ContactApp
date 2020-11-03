package com.example.cft1stproject

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.contact_found_layout.*
import java.net.URI

class FoundContactFragment(private val contactView: ContactView, var number: String): Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_found_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contact_name.text = contactView.name
        val imageURI = Uri.parse(contactView.imageUri)
        contact_image.setImageURI(imageURI)
        contact_number.text = contactView.number
        super.onViewCreated(view, savedInstanceState)
    }
}