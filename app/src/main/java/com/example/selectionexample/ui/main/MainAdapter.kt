package com.example.selectionexample.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.selectionexample.ElementModel
import com.example.selectionexample.R

class MainAdapter(
    private val list: MutableList<ElementModel> = mutableListOf(),
    private val onItemInteraction: (adapterPosition: Int) -> Unit
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_fragment_recycler_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position, onItemInteraction)
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
                    list[oldItemPosition].isChecked == newList[newItemPosition].isChecked
            }
        })

    class ViewHolder(
        private val view: View,
    ) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.tv_text)
        private val cb: CheckBox = view.findViewById(R.id.cb_selection)

        fun bind(
            element: ElementModel,
            adapterPosition: Int,
            onItemInteraction: (adapterPosition: Int) -> Unit) {
            if (element.containsGeoData) {
                textView.text = element.text + "GEO"
            } else textView.text = element.text

            cb.isChecked = element.isChecked
            view.setOnClickListener {
                cb.performClick()
            }
            cb.setOnClickListener {
                onItemInteraction(adapterPosition)
            }
        }
    }
}
