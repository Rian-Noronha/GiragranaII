package com.rn.giragrana.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.rn.giragrana.common.SingleLiveEvent
import com.rn.giragrana.model.Resale
import com.rn.giragrana.repository.ResaleRepository

class ResaleListViewModel(
    private val repository: ResaleRepository
) : ViewModel(){

    var resaleIdSelected: Long = -1
    private val searchTerm = MutableLiveData<String>()
    private val resales: LiveData<List<Resale>> = searchTerm.switchMap { term ->
        repository.search("%$term")
    }
    private val inDeleteMode = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val selectedItems = mutableListOf<Resale>()
    private val selectionCount = MutableLiveData<Int>()
    private val selectedResales = MutableLiveData<List<Resale>>().apply {
        value = selectedItems
    }
    private val deletedItems = mutableListOf<Resale>()
    private val showDeletedMessage = SingleLiveEvent<Int>()
    private val showDetailsCommand = SingleLiveEvent<Resale>()

    fun isInDeleteMode(): LiveData<Boolean> = inDeleteMode

    fun getSearchTerm(): LiveData<String>? = searchTerm

    fun getResales(): LiveData<List<Resale>>? = resales

    fun selectionCount(): LiveData<Int> = selectionCount

    fun selectedResales(): LiveData<List<Resale>> = selectedResales

    fun showDeletedMessage(): LiveData<Int> = showDeletedMessage

    fun showDetailsCommand(): LiveData<Resale> = showDetailsCommand


    fun selectResale(resale: Resale) {
        if (inDeleteMode.value == true) {
            toggleResaleSelected(resale)
            if (selectedItems.size == 0) {
                inDeleteMode.value = false
            } else {
                selectionCount.value = selectedItems.size
                selectedResales.value = selectedItems
            }
        } else {
            showDetailsCommand.value = resale
        }
    }
    private fun toggleResaleSelected(resale: Resale) {
        val existing = selectedItems.find { it.id == resale.id }
        if (existing == null) {
            selectedItems.add(resale)
        } else {
            selectedItems.removeAll { it.id == resale.id }
        }
    }
    fun search(term: String = "") {
        searchTerm.value = term
    }
    fun setInDeleteMode(deleteMode: Boolean) {
        if (!deleteMode) {
            selectionCount.value = 0
            selectedItems.clear()
            selectedResales.value = selectedItems
            showDeletedMessage.value = selectedItems.size
        }
        inDeleteMode.value = deleteMode
    }

    fun deleteSelected() {
        repository.remove(*selectedItems.toTypedArray())
        deletedItems.clear()
        deletedItems.addAll(selectedItems)
        setInDeleteMode(false)
        showDeletedMessage.value = deletedItems.size
    }
    fun undoDelete() {
        if (deletedItems.isNotEmpty()) {
            for (resale in deletedItems) {
                resale.id = 0L
                repository.save(resale)
            }
        }
    }

}