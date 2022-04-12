package com.example.selectionexample.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.selectionexample.ElementModel
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _elementLiveData = MutableLiveData<List<ElementModel>>()
    val elementLiveData :LiveData<List<ElementModel>> = _elementLiveData

    val selectAllButtonState: LiveData<SelectAllButtonState> = Transformations.map(elementLiveData) { list ->
        if (list.firstOrNull { !it.isChecked } != null) SelectAllButtonState.SelectAll
        else SelectAllButtonState.DeselectAll
    }

    val selectAllGeoButtonState: LiveData<SelectAllGeoButtonState> = Transformations.map(elementLiveData) {list ->
        if (list.firstOrNull { it.containsGeoData && !it.isChecked } != null) SelectAllGeoButtonState.Available
        else SelectAllGeoButtonState.Inactive
    }

    val selectedItems: LiveData<List<ElementModel>> = Transformations.map(elementLiveData) { list->
        list.filter { it.isChecked }
    }

    init {
        for (i in 0..15) {
            _elementLiveData.imutPlus(ElementModel(text = "element $i", containsGeoData = Random.nextBoolean()))
        }
    }

    fun changeListSelection(selectAll: Boolean) {
        if (selectAll) {
            val newList= _elementLiveData.value?.map {
                it.copy(isChecked = true)
            } ?: mutableListOf()
            _elementLiveData.postValue(newList)

        } else {
            val newList = _elementLiveData.value?.map {
                it.copy(isChecked = false)
            } ?: mutableListOf()
            _elementLiveData.postValue(newList)

        }
    }

    fun selectGeo() {
        val newList= _elementLiveData.value?.map {
            if (it.containsGeoData) {
                it.copy(isChecked = true)
            }
            else it.copy(isChecked = false)
        } ?: mutableListOf()
        _elementLiveData.postValue(newList)
    }

    fun onItemClicked(position: Int) {
        _elementLiveData.value?.let { list ->
            list[position].isChecked = !list[position].isChecked
            _elementLiveData.postValue(list)
        }
        Log.d("dragu", "viewModel onItemClicked position = $position")
    }

}

// for mutable list
fun <T> MutableLiveData<MutableList<T>>.mutPlus(item: T) {
    val value = this.value ?: mutableListOf()
    value.add(item)
    this.value = value
}

// for immutable list
fun <T> MutableLiveData<List<T>>.imutPlus(item: T) {
    val value = this.value ?: emptyList()
    this.value = value + listOf(item)
}
