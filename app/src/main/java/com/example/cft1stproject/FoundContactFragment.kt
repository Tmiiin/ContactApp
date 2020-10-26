package com.example.cft1stproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class FoundContactFragment(var name: String, var number: String): Fragment(){

    lateinit var rootView: View
    lateinit var contactNumber: TextView
    lateinit var contactName: TextView
    lateinit var contactImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.contact_found_layout, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contactNumber = view.findViewById(R.id.contact_number)
        contactImage = view.findViewById(R.id.contact_image)
        contactName = view.findViewById(R.id.contact_name)
        contactName.text = name
        contactNumber.text = number
        super.onViewCreated(view, savedInstanceState)
    }
}