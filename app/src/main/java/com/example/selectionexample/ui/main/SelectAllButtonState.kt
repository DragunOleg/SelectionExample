package com.example.selectionexample.ui.main

sealed class SelectAllButtonState {
    object SelectAll : SelectAllButtonState()
    object DeselectAll : SelectAllButtonState()
}

sealed class SelectAllGeoButtonState {
    object Available : SelectAllGeoButtonState()
    object Inactive : SelectAllGeoButtonState()
}