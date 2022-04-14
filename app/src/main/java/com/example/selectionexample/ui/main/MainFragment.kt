package com.example.selectionexample.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionPredicate
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.selectionexample.R
import com.example.selectionexample.ui.selection.MainItemDetailsLookup
import com.example.selectionexample.ui.selection.MainItemKeyProvider

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var listAdapter: MainAdapter
    private lateinit var recyclerView: RecyclerView
    private var tracker: SelectionTracker<String>? = null

    private lateinit var parent: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parent = inflater.inflate(R.layout.main_fragment, container, false)
        return parent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        listAdapter = MainAdapter()
        recyclerView = parent.findViewById<RecyclerView?>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        initSelectionTracker(savedInstanceState)
        tracker?.let { listAdapter.setTracker(it) }
        viewModel.elementLiveData.observe(viewLifecycleOwner) {
            listAdapter.update(it)
        }

        setupButtons()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        tracker?.onSaveInstanceState(savedInstanceState)
    }

    private fun initSelectionTracker(savedInstanceState: Bundle?) {

        tracker = SelectionTracker.Builder(
            "MainSelection",
            recyclerView,
            MainItemKeyProvider(listAdapter),
            MainItemDetailsLookup(recyclerView),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
            object : SelectionPredicate<String>() {
                override fun canSetStateForKey(key: String, nextState: Boolean): Boolean {
                    listAdapter.list.forEach {
                        if (key == it.text) return true
                    }
                    return false
                }

                override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
                    return true
                }

                override fun canSelectMultiple(): Boolean {
                    return true
                }
            }
        ).build()

        if (savedInstanceState != null) {
            tracker!!.onRestoreInstanceState(savedInstanceState)
        }

        tracker!!.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            private var hasSelection = false
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                if (hasSelection != tracker!!.hasSelection()) {
                    hasSelection = tracker!!.hasSelection()
                    Log.d("dragu", "onSelectionChanged to $hasSelection")
                    recyclerView.post { listAdapter.notifyDataSetChanged() }
                }
                parent.findViewById<Button>(R.id.btn_select_deselect).also { button ->
                    if (listAdapter.list.size == tracker!!.selection.size()) {
                        button.text = "Deselect all"
                    } else {
                        button.text = "Select all"
                    }
                }

                val selection = tracker!!.selection.toSet()
                val recyclerSet = listAdapter.list.filter { it.containsGeoData }.map {
                    it.text
                }.toSet()
                parent.findViewById<Button>(R.id.btn_select_geo).also { button ->
                    button.isEnabled = selection!=recyclerSet
                }

                parent.findViewById<Button>(R.id.btn_send).isEnabled = hasSelection



            }
        })

    }

    private fun setupButtons() {
        parent.findViewById<Button>(R.id.btn_send).also { button ->
            val selectedList = tracker?.selection
            selectedList?.forEach {
                Log.d("dragu", "item selected: $it")
            }

            button.setOnClickListener {
                Toast.makeText(
                    context,
                    "${selectedList?.size()} element send",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        parent.findViewById<Button>(R.id.btn_select_deselect).also { button ->
            button.setOnClickListener {
                val itemKeys: List<String> = listAdapter.list.map { it.text }
                tracker?.setItemsSelected(
                    itemKeys,
                    tracker?.selection?.size() ?: 0 != itemKeys.size
                )
                listAdapter.notifyDataSetChanged()
            }
        }

        parent.findViewById<Button>(R.id.btn_select_geo).setOnClickListener {
            val newSelectedList =mutableListOf<String>()
            listAdapter.list.forEach { element ->
                if (element.containsGeoData) {
                    newSelectedList.add(element.text)
                }
            }
            tracker?.clearSelection()
            tracker?.setItemsSelected(newSelectedList, true)
        }
    }
}