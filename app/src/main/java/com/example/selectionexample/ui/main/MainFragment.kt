package com.example.selectionexample.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.selectionexample.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var listAdapter: MainAdapter
    private lateinit var recyclerView: RecyclerView

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
        listAdapter = MainAdapter { viewModel.onItemClicked(it) }
        recyclerView = parent.findViewById<RecyclerView?>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
        viewModel.elementLiveData.observe(viewLifecycleOwner) {
            listAdapter.update(it)
        }
        parent.findViewById<Button>(R.id.btn_select_deselect).also { button ->
            viewModel.selectAllButtonState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    SelectAllButtonState.SelectAll -> {
                        button.text = "SelectAll all"
                        button.setOnClickListener {
                            viewModel.changeListSelection(selectAll = true)
                        }
                    }
                    SelectAllButtonState.DeselectAll -> {
                        button.text = "DeselectAll all"
                        button.setOnClickListener {
                            viewModel.changeListSelection(selectAll = false)
                        }
                    }
                }
            }
        }

        parent.findViewById<Button>(R.id.btn_select_geo).also { button ->
            viewModel.selectAllGeoButtonState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    SelectAllGeoButtonState.Inactive -> {
                        button.isEnabled = false
                    }
                    SelectAllGeoButtonState.Available -> {
                        button.isEnabled = true
                        button.setOnClickListener {
                            viewModel.selectGeo()
                        }
                    }
                }
            }
        }

        parent.findViewById<Button>(R.id.btn_send).also { button ->
            viewModel.selectedItems.observe(viewLifecycleOwner) { list ->
                if (list.isEmpty()) {
                    button.isEnabled = false
                } else {
                    button.isEnabled = true
                    button.setOnClickListener {
                        Toast.makeText(context, "${list.size} element send", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

}