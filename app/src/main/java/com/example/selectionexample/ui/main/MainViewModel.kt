package com.example.selectionexample.ui.main

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
        for (i in 0..5) {
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
