package com.example.selectionexample.ui.selection

import androidx.recyclerview.selection.ItemKeyProvider
import com.example.selectionexample.ui.main.MainAdapter

class MainItemKeyProvider(private val adapter: MainAdapter) : ItemKeyProvider<String>(
    SCOPE_CACHED
) {

    override fun getKey(position: Int): String {
        return adapter.list[position].text
    }

    override fun getPosition(key: String): Int {
        return adapter.list.indexOfFirst { it.text == key }
    }
}
