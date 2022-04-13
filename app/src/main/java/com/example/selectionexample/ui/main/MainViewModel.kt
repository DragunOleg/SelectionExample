package com.example.selectionexample.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.selectionexample.ElementModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _elementLiveData = MutableLiveData<List<ElementModel>>()
    val elementLiveData: LiveData<List<ElementModel>> = _elementLiveData

    init {
        for (i in 0..15) {
            this.viewModelScope.launch {
                delay(500 * i.toLong())
                _elementLiveData.imutPlus(
                    ElementModel(
                        text = "element $i",
                        containsGeoData = Random.nextBoolean()
                    )
                )
            }
        }
    }

    fun changeListSelection(selectAll: Boolean) {
        val newList = _elementLiveData.value?.map {
            it.copy(isChecked = selectAll)
        } ?: mutableListOf()
        _elementLiveData.postValue(newList)
    }

    fun selectGeo() {
        val newList = _elementLiveData.value?.map {
            it.copy(isChecked = it.containsGeoData)
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

/**
 * add element to mutable list
 */
fun <T> MutableLiveData<MutableList<T>>.mutPlus(item: T) {
    val value = this.value ?: mutableListOf()
    value.add(item)
    this.value = value
}

/**
 * add element to immutable list
 */
fun <T> MutableLiveData<List<T>>.imutPlus(item: T) {
    val value = this.value ?: emptyList()
    this.value = value + listOf(item)
}
