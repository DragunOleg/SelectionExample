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
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.selectionexample.R
import com.example.selectionexample.ui.selection.MainItemDetailsLookup
import com.example.selectionexample.ui.selection.MainItemKeyProvider
import com.example.selectionexample.ui.selection.MainTrackerObserver

class MainFragment : Fragment() {

    companion object {
        private val TAG = MainFragment::class.java.simpleName
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var listAdapter: MainAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var btnSelectDeselect: Button? = null
    private var btnSelectGeo: Button? = null
    private var btnSend: Button? = null
    private var tracker: SelectionTracker<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        listAdapter = MainAdapter(
            isItemSelected = { tracker?.isSelected(it) ?: false },
            selectItem = { tracker?.select(it) }
        )
        recyclerView = view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
        btnSelectDeselect = view.findViewById(R.id.btn_select_deselect)
        btnSelectGeo = view.findViewById(R.id.btn_select_geo)
        btnSend = view.findViewById(R.id.btn_send)

        initSelectionTracker(savedInstanceState)
        viewModel.elementLiveData.observe(viewLifecycleOwner) {
            listAdapter?.update(it)
            refreshNonRecyclerItems()
        }
        setupClickListeners()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        tracker?.onSaveInstanceState(savedInstanceState)
    }

    private fun initSelectionTracker(savedInstanceState: Bundle?) {
        recyclerView?.let { rv ->
            listAdapter?.let { adapter ->
                tracker = SelectionTracker.Builder(
                    TAG,
                    rv,
                    MainItemKeyProvider(adapter),
                    MainItemDetailsLookup(rv),
                    StorageStrategy.createStringStorage()
                ).build()
            } ?: throw IllegalArgumentException("No adapter to pin selection tracker to!")
        } ?: throw IllegalArgumentException("No recycler to pin selection tracker to!")
        tracker?.addObserver(MainTrackerObserver { refreshNonRecyclerItems() })
            ?: throw IllegalArgumentException("No tracker provided!")
        /**
         * call it after we add observer, so restored Instance on fragment gonna represent restored selection
         */
        if (savedInstanceState != null) {
            tracker?.onRestoreInstanceState(savedInstanceState)
        }
    }

    private fun refreshNonRecyclerItems() {
        val selectionSet = tracker?.selection?.toSet() ?: setOf()
        val recyclerGeoSet = listAdapter?.list?.filter { it.containsGeoData }?.map {
            it.id
        }?.toSet() ?: setOf()

        btnSelectDeselect?.apply {
            text = if (listAdapter?.itemCount == selectionSet.size) getString(R.string.deselect_all)
            else getString(R.string.select_all)
        }
        btnSelectGeo?.isEnabled = selectionSet != recyclerGeoSet
        btnSend?.isEnabled = selectionSet.isNotEmpty()
    }

    private fun setupClickListeners() {
        btnSend?.setOnClickListener {
            Toast.makeText(
                context,
                "${tracker?.selection?.size()} elements send",
                Toast.LENGTH_SHORT
            ).show()
        }
        btnSelectDeselect?.setOnClickListener {
            val itemKeys: List<String> = listAdapter?.list?.map { it.id } ?: listOf()
            tracker?.setItemsSelected(
                itemKeys,
                tracker?.selection?.size() != itemKeys.size
            )
        }
        btnSelectGeo?.setOnClickListener {
            tracker?.clearSelection()
            tracker?.setItemsSelected(
                listAdapter?.list
                    ?.filter { it.containsGeoData }
                    ?.map { it.id } ?: listOf(),
                true
            )
        }
    }
}