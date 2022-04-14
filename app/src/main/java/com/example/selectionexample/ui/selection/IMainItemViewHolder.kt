package com.example.selectionexample.ui.selection

import androidx.recyclerview.selection.ItemDetailsLookup

interface IMainItemViewHolder {
    fun getItemDetails(): ItemDetailsLookup.ItemDetails<String>
}