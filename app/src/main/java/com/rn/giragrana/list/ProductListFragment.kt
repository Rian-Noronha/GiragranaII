package com.rn.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rn.giragrana.R
import com.rn.giragrana.common.AboutDialogFragment
import com.rn.giragrana.databinding.FragmentListProductBinding
import com.rn.giragrana.model.Product
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProductListFragment :
ListFragment(),
AdapterView.OnItemLongClickListener,
ActionMode.Callback {

    private val viewModel: ProductListViewModel by sharedViewModel()
    private var actionMode: ActionMode? = null
    private lateinit var binding: FragmentListProductBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListProductBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView: ListView = view.findViewById(android.R.id.list)
        listView.onItemLongClickListener = this
        viewModel.showDetailsCommand().observe(viewLifecycleOwner, Observer { product ->
            if (product != null) {
                showProductDetails(product)
            }
        })
        viewModel.isInDeleteMode().observe(viewLifecycleOwner, Observer { deleteMode ->
            if (deleteMode == true) {
                showDeleteMode()
            } else {
                hideDeleteMode()
            }
        })
        viewModel.selectedProducts().observe(viewLifecycleOwner, Observer { products ->
            if (products != null) {
                showSelectedProducts(products)
            }
        })
        viewModel.selectionCount().observe(viewLifecycleOwner, Observer { count ->
            if (count != null) {
                updateSelectionCountText(count)
            }
        })
        viewModel.showDeletedMessage().observe(viewLifecycleOwner, Observer { count ->
            if (count != null && count > 0) {
                showMessageProductsDeleted(count)
            }
        })
        viewModel.getProducts()?.observe(viewLifecycleOwner, Observer { products ->
            if (products != null) {
                showProducts(products)
            }
        })



        binding.fabAdd.setOnClickListener {
            hideDeleteMode()
            navigateToProductFormFragment()
        }


        if (viewModel.getProducts()?.value == null) {
            search()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_client ->
                navigateToClientListFragment()
            R.id.action_info -> {
                AboutDialogFragment().show(parentFragmentManager, "sobre")
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun navigateToClientListFragment(){
        val action = ProductListFragmentDirections
            .actionFragmentListProductToFragmentListClient()
        findNavController().navigate(action)
    }

    private fun navigateToProductFormFragment(){
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_fragmentListProduct_to_productFormFragment)
    }

    private fun showProducts(products: List<Product>) {
        val adapter = ProductAdapter(requireContext(), products)
        listAdapter = adapter
    }

    private fun showProductDetails(product: Product) {
        if (activity is OnProductClickListener) {
            val listener = activity as OnProductClickListener
            listener.onProductClick(product)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val product = l?.getItemAtPosition(position) as Product
        viewModel.selectProduct(product)
        if(actionMode == null){
            val action = ProductListFragmentDirections
                .actionFragmentListProductToProductDetailsFragment(productId = product.id)
            findNavController().navigate(action)
        }

    }

    fun search(text: String = "") {
        viewModel.search(text)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?,
                                 position: Int, id: Long): Boolean {
        val consumed = (actionMode == null)
        if (consumed) {
            val product = parent?.getItemAtPosition(position) as Product
            viewModel.setInDeleteMode(true)
            viewModel.selectProduct(product)
        }
        return consumed
    }

    private fun showDeleteMode() {
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    fun hideDeleteMode() {
        listView.onItemLongClickListener = this
        for (i in 0 until listView.count) {
            listView.setItemChecked(i, false)
        }
        listView.post {
            actionMode?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }
    }

    private fun updateSelectionCountText(count: Int) {
        view?.post {
            actionMode?.title = resources.getQuantityString(R.plurals.list_product_selected, count, count)
        }
    }

    private fun showSelectedProducts(products: List<Product>) {
        listView.post {
            for (i in 0 until listView.count) {
                val product = listView.getItemAtPosition(i) as Product
                if (products.find { it.id == product.id } != null) {
                    listView.setItemChecked(i, true)
                }
            }
        }
    }

    private fun showMessageProductsDeleted(count: Int) {
        Snackbar.make(listView,
            getString(R.string.message_products_deleted, count),
            Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                viewModel.undoDelete()
            }
            .show()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete_product) {
            viewModel.deleteSelected()
            return true
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        activity?.menuInflater?.inflate(R.menu.product_delete_list, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        viewModel.setInDeleteMode(false)
    }

    interface OnProductClickListener {
        fun onProductClick(product: Product)
    }

}