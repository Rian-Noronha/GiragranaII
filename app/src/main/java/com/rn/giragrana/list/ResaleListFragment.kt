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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentListResaleBinding
import com.rn.giragrana.model.Client
import com.rn.giragrana.model.Product
import com.rn.giragrana.model.Resale
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@Suppress("DEPRECATION")
class ResaleListFragment :
    ListFragment(),
    AdapterView.OnItemLongClickListener,
    ActionMode.Callback {

    private val viewModelResale: ResaleListViewModel by sharedViewModel()
    private val viewModelProduct: ProductListViewModel by sharedViewModel()
    private val viewModelClient: ClientListViewModel by sharedViewModel()
    private var actionMode: ActionMode? = null
    private lateinit var binding: FragmentListResaleBinding
    private var productsMap: Map<Long, Product> = emptyMap()
    private var clientsMap: Map<Long, Client> = emptyMap()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListResaleBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView: ListView = view.findViewById(android.R.id.list)
        listView.onItemLongClickListener = this
        viewModelResale.showDetailsCommand().observe(viewLifecycleOwner) { resale ->
            if (resale != null) {
                showResaleDetails(resale)
            }
        }
        viewModelResale.isInDeleteMode().observe(viewLifecycleOwner) { deleteMode ->
            if (deleteMode == true) {
                showDeleteMode()
            } else {
                hideDeleteMode()
            }
        }
        viewModelResale.selectedResales().observe(viewLifecycleOwner) { resales ->
            if (resales != null) {
                showSelectedResales(resales)
            }
        }
        viewModelResale.selectionCount().observe(viewLifecycleOwner) { count ->
            if (count != null) {
                updateSelectionCountText(count)
            }
        }
        viewModelResale.showDeletedMessage().observe(viewLifecycleOwner) { count ->
            if (count != null && count > 0) {
                showMessageResalesDeleted(count)
            }
        }
        viewModelResale.getResales()?.observe(viewLifecycleOwner) { resales ->
            if (resales != null) {
                showResales(resales)
            }
        }


        if (viewModelResale.getResales()?.value == null) {
            search()
        }

        viewModelProduct.productsMap.observe(viewLifecycleOwner) { map ->
            map?.let {
                productsMap = it
                updateResaleList()
            }
        }

        viewModelClient.clientsMap.observe(viewLifecycleOwner) { map ->
            map?.let {
                clientsMap = it
                updateResaleList()
            }
        }

    }


    private fun updateResaleList() {
        val resales = viewModelResale.getResales()?.value
        if (resales != null) {
            showResales(resales)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.resale, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                navigateToProductListFragment()

            R.id.action_new_resale -> {
                navigateToResaleFormFragment()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun navigateToResaleFormFragment() {
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_resaleListFragment_to_resaleFormFragment)
    }

    private fun navigateToProductListFragment() {
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.popBackStack(R.id.fragmentListProduct, false)
        navController.navigate(R.id.fragmentListProduct)
    }


    private fun showResales(resales: List<Resale>) {
        val adapter = ResaleAdapter(
            requireContext(),
            resales,
            productsMap,
            clientsMap
        )
        listAdapter = adapter
    }

    private fun showResaleDetails(resale: Resale) {
        if (activity is OnResaleClickListener) {
            val listener = activity as OnResaleClickListener
            listener.onResaleClick(resale)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val resale = l.getItemAtPosition(position) as Resale
        viewModelResale.selectResale(resale)
        if (actionMode == null) {
            val action = ResaleListFragmentDirections
                .actionResaleListFragmentToResaleFormFragment(resaleId = resale.id)
            findNavController().navigate(action)
        }

    }

    fun search(text: String = "") {
        viewModelResale.search(text)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?, view: View?,
        position: Int, id: Long
    ): Boolean {
        val consumed = (actionMode == null)
        if (consumed) {
            val resale = parent?.getItemAtPosition(position) as Resale
            viewModelResale.setInDeleteMode(true)
            viewModelResale.selectResale(resale)
        }
        return consumed
    }

    private fun showDeleteMode() {
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun hideDeleteMode() {
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
            actionMode?.title =
                resources.getQuantityString(R.plurals.list_product_selected, count, count)
        }
    }

    private fun showSelectedResales(resales: List<Resale>) {
        listView.post {
            for (i in 0 until listView.count) {
                val resale = listView.getItemAtPosition(i) as Resale
                if (resales.find { it.id == resale.id } != null) {
                    listView.setItemChecked(i, true)
                }
            }
        }
    }

    private fun showMessageResalesDeleted(count: Int) {
        Snackbar.make(
            listView,
            getString(R.string.message_resales_deleted, count),
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.undo) {
                viewModelResale.undoDelete()
            }
            .show()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete_resale) {
            viewModelResale.deleteSelected()
            return true
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        activity?.menuInflater?.inflate(R.menu.resale_delete_list, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        viewModelResale.setInDeleteMode(false)
    }

    interface OnResaleClickListener {
        fun onResaleClick(resale: Resale)
    }

    companion object


}