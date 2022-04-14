package com.example.selectionexample.ui.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MainItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        view?.let {
            val holder = recyclerView.getChildViewHolder(it)
            if (holder is IMainItemViewHolder) {
                return holder.getItemDetails()
            }
        }

        return null
    }
}