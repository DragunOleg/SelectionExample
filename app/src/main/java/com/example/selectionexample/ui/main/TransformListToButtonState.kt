package com.example.selectionexample.ui.main

import com.example.selectionexample.ElementModel

class TransformListToButtonState() {
    fun listToSelectAllButtonState(list: List<ElementModel>): SelectAllButtonState =
        if (list.firstOrNull { !it.isChecked } != null) SelectAllButtonState.SelectAll
        else SelectAllButtonState.DeselectAll


    fun listToSelectAllGeoButtonState(list: List<ElementModel>): SelectAllGeoButtonState =
        if (list.firstOrNull { it.containsGeoData && !it.isChecked } != null) SelectAllGeoButtonState.Available
        else SelectAllGeoButtonState.Inactive
}