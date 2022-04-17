package com.example.selectionexample.ui.selection

import androidx.recyclerview.selection.SelectionTracker

/**
This observer is extra one, on top of base tracker oberver.
Base tracker observer is calling onBindViewHolder for
a) single item selection (selectionTracker.select(elementKey)) on clickListener
b) multiple selection through "tracker.setItemsSelected" for each item selection state change
c) Deselect item made BY LIBRARY. NO OWN IMPLEMENTATION NEEDED

So DO NOT put any adapter.notifyDataSetChange in this extra observer.
Do selection state change displaying ONLY in onBind method
 */
class MainTrackerObserver(
    private val refreshNonRecyclerItems: () -> Unit
) : SelectionTracker.SelectionObserver<String>() {

    override fun onSelectionChanged() {
        super.onSelectionChanged()
        refreshNonRecyclerItems()
    }

    /**
     * as soon as we are calling refreshNonRecyclerItems on subscribing to elementLiveData
     * = no need to trigger it here.
     * With some different approach use it
     */
    override fun onSelectionRestored() {
        super.onSelectionRestored()
        //refreshNonRecyclerItems.invoke()
    }
}