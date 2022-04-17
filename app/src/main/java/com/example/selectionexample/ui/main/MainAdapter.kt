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
    val list: MutableList<ElementModel> = mutableListOf(),
    private val isItemSelected: (itemKey: String) -> Boolean,
    private val selectItem: (itemKey: String) -> Unit
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_fragment_recycler_element, parent, false)
        return ViewHolder(view, isItemSelected, selectItem)
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
                return list[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition].text == newList[newItemPosition].text
            }
        })

    class ViewHolder(
        private val view: View,
        private val isItemSelected: (itemKey: String) -> Boolean,
        private val selectItem: (itemKey: String) -> Unit
    ) : RecyclerView.ViewHolder(view), IMainItemViewHolder {
        private lateinit var id: String
        private val textView: TextView = view.findViewById(R.id.tv_text)
        private val cb: CheckBox = view.findViewById(R.id.cb_selection)

        fun bind(element: ElementModel) {
            id = element.id
            cb.isChecked = isItemSelected(id)
            textView.text = if (element.containsGeoData) element.text + "GEO"
            else element.text
            /**
             * Make attention, we are not handling "deselect" event.
             * That's because library made it by itselft though catching touch even for aree we give
             * it in DetailsLookup
             */
            view.setOnClickListener { selectItem(id) }
            cb.setOnClickListener { selectItem(id) }
        }

        override fun getItemDetails(): ItemDetails<String> {

            return object : ItemDetails<String>() {
                override fun getPosition(): Int {
                    return adapterPosition
                }

                override fun getSelectionKey(): String {
                    return id
                }
            }
        }
    }
}
