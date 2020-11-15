package com.example.cft1stproject.model

data class ContactView(
    val name: String,
    val imageUri: String,
    val number: String
) {

    fun isNullOrEmpty(): Boolean {
        return name.isEmpty()
    }
}