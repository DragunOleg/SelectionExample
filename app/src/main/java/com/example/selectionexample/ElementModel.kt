package com.example.selectionexample

data class ElementModel(
    val text: String,
    val containsGeoData: Boolean,
    var isChecked: Boolean = false
)
