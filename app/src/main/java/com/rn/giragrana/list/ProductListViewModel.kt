package com.rn.giragrana.list
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.rn.giragrana.common.SingleLiveEvent
import com.rn.giragrana.model.Product
import com.rn.giragrana.repository.ProductRepository

class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel(){

    var productIdSelected: Long = -1
    private val searchTerm = MutableLiveData<String>()
    private val products: LiveData<List<Product>> = searchTerm.switchMap { term ->
        repository.search("%$term")
    }
    private val inDeleteMode = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val selectedItems = mutableListOf<Product>()
    private val selectionCount = MutableLiveData<Int>()
    private val selectedProducts = MutableLiveData<List<Product>>().apply {
        value = selectedItems
    }
    private val deletedItems = mutableListOf<Product>()
    private val showDeletedMessage = SingleLiveEvent<Int>()
    private val showDetailsCommand = SingleLiveEvent<Product>()

    fun isInDeleteMode(): LiveData<Boolean> = inDeleteMode

    private val _productsMap = MutableLiveData<Map<Long, Product>>()
    val productsMap: LiveData<Map<Long, Product>> get() = _productsMap

    init {
        repository.getProductsMap().observeForever { map ->
            _productsMap.value = map
        }
    }

    private val unsoldProducts: LiveData<List<Product>> = repository.unsoldProducts()

    fun getSearchTerm(): LiveData<String>? = searchTerm

    fun getProducts(): LiveData<List<Product>>? = products

    fun getUnsoldProducts(): LiveData<List<Product>>? = unsoldProducts

    fun selectionCount(): LiveData<Int> = selectionCount

    fun selectedProducts(): LiveData<List<Product>> = selectedProducts

    fun showDeletedMessage(): LiveData<Int> = showDeletedMessage

    fun showDetailsCommand(): LiveData<Product> = showDetailsCommand


    fun productById(productId: Long): LiveData<Product>{
        return repository.productById(productId)
    }


    fun selectProduct(product: Product) {
        if (inDeleteMode.value == true) {
            toggleProductSelected(product)
            if (selectedItems.size == 0) {
                inDeleteMode.value = false
            } else {
                selectionCount.value = selectedItems.size
                selectedProducts.value = selectedItems
            }
        } else {
            showDetailsCommand.value = product
        }
    }
    private fun toggleProductSelected(product: Product) {
        val existing = selectedItems.find { it.id == product.id }
        if (existing == null) {
            selectedItems.add(product)
        } else {
            selectedItems.removeAll { it.id == product.id }
        }
    }
    fun search(term: String = "") {
        searchTerm.value = term
    }
    fun setInDeleteMode(deleteMode: Boolean) {
        if (!deleteMode) {
            selectionCount.value = 0
            selectedItems.clear()
            selectedProducts.value = selectedItems
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
            for (hotel in deletedItems) {
                hotel.id = 0L
                repository.save(hotel)
            }
        }
    }



}