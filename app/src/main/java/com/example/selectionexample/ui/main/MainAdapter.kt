package com.example.selectionexample.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.selectionexample.ElementModel
import com.example.selectionexample.R
import com.example.selectionexample.ui.selection.IMainItemViewHolder

class MainAdapter(
    val list: MutableList<ElementModel> = mutableListOf()
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    lateinit var selectionTracker: SelectionTracker<String>

    fun setTracker(tracker: SelectionTracker<String>) {
        selectionTracker = tracker
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_fragment_recycler_element, parent, false)
        return ViewHolder(view, selectionTracker)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun update(newList: List<ElementModel>) {
        val diff = calculateDiff(newList)
        list.clear()
        list.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    private fun calculateDiff(newList: List<ElementModel>): DiffUtil.DiffResult =
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                //return dataSet[oldItemPosition].itemType == newList[newItemPosition].itemType
                return true
            }

            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition].text == newList[newItemPosition].text &&
                    list[oldItemPosition].containsGeoData == newList[newItemPosition].containsGeoData
            }
        })

    class ViewHolder(
        private val view: View,
        private val selectionTracker: SelectionTracker<String>
    ) : RecyclerView.ViewHolder(view), IMainItemViewHolder {
        private val textView: TextView = view.findViewById(R.id.tv_text)
        private val cb: CheckBox = view.findViewById(R.id.cb_selection)

        fun bind(
            element: ElementModel
        ) {
            val isSelected = selectionTracker.isSelected(element.text)
            if (cb.isChecked != isSelected) {
                //this will make visible animation for cb
                val successfull = cb.post { cb.isChecked = isSelected }
                //if something is wrong with runnable, still make sure model and element are similar
                if (!successfull) cb.isChecked = isSelected
            }


            if (element.containsGeoData) {
                textView.text = element.text + "GEO"
            } else textView.text = element.text
            view.setOnClickListener {
                selectionTracker.select(element.text)
            }
            cb.setOnClickListener {
                selectionTracker.select(element.text)
            }
        }

        override fun getItemDetails(): ItemDetails<String> {

            return object: ItemDetails<String>() {
                override fun getPosition(): Int {
                    return adapterPosition
                }

                override fun getSelectionKey(): String {
                    return textView.text.toString().removeSuffix("GEO")
                }
            }


        }


    }
}
