package com.rn.giragrana.list
import android.os.Bundle
import androidx.appcompat.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentListClientBinding
import com.rn.giragrana.model.Client
import com.rn.giragrana.utils.PdfUtils.exportToPdf
import com.rn.giragrana.utils.ShareUtils.sharePdf
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

class ClientListFragment :
    ListFragment(),
    AdapterView.OnItemLongClickListener,
    ActionMode.Callback{
    private val viewModel: ClientListViewModel by sharedViewModel()
    private var actionMode: ActionMode? = null
    private lateinit var binding: FragmentListClientBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListClientBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView: ListView = view.findViewById(android.R.id.list)
        listView.onItemLongClickListener = this
        viewModel.showDetailsCommand().observe(viewLifecycleOwner, Observer { client ->
            if(client != null){
                showClientDetails(client)
            }
        })

        viewModel.isInDeleteMode().observe(viewLifecycleOwner, Observer { deleteMode ->
            if(deleteMode == true){
                showDeleteMode()
            }else{
                hideDeleteMode()
            }
        })

        viewModel.selectedClients().observe(viewLifecycleOwner, Observer { clients ->
            if(clients != null){
                showSelectedClients(clients)
            }
        })

        viewModel.selectionCount().observe(viewLifecycleOwner, Observer { count ->
            if(count != null){
                updateSelectionCountText(count)
            }
        })

        viewModel.showDeletedMessage().observe(viewLifecycleOwner, Observer { count ->
            if(count != null && count > 0){
                showMessageClientsDeleted(count)
            }
        })

        viewModel.getClients()?.observe(viewLifecycleOwner, Observer { clients ->
            if(clients != null){
                showClients(clients)
            }
        })

        if(viewModel.getClients()?.value == null){
            search()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.client, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){

            android.R.id.home -> {
                navigateToProductListFragment()
            }

            R.id.action_new_client ->
                navigateToClientFormFrament()

            R.id.action_want_pdf -> {
                val file = File(requireContext().externalCacheDir, "meus_clientes.pdf")
                exportToPdf(listView, "Meus Clientes", file)
                sharePdf(requireContext(), file)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val client = l?.getItemAtPosition(position) as Client
        viewModel.selectClient(client)
        if(actionMode == null){
            val action = ClientListFragmentDirections
                .actionFragmentListClientToClientFormFragment(clientId = client.id)
            findNavController().navigate(action)
        }
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val consumed = (actionMode == null)
        if(consumed){
            val client = parent?.getItemAtPosition(position) as Client
            viewModel.setInDeleteMode(true)
            viewModel.selectClient(client)
        }

        return consumed
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        activity?.menuInflater?.inflate(R.menu.client_delete_list, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false


    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.action_delete_client){
            viewModel.deleteSelected()
            return true
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        viewModel.setInDeleteMode(false)
    }

    fun showClientDetails(client: Client){
        if(activity is OnClientClickListener){
            val listener = activity as OnClientClickListener
            listener.onClientClick(client)
        }
    }

    fun showDeleteMode(){
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    fun hideDeleteMode(){
        listView.onItemLongClickListener = this
        for(i in 0 until listView.count){
            listView.setItemChecked(i, false)
        }

        listView.post{
            actionMode?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }

    }

    fun showSelectedClients(clients: List<Client>){
        listView.post{
            for(i in 0 until listView.count){
                val client = listView.getItemAtPosition(i) as Client
                if(clients.find{it.id == client.id} != null){
                    listView.setItemChecked(i, true)
                }
            }
        }
    }

    fun updateSelectionCountText(count: Int){
        view?.post{
            actionMode?.title = resources.getQuantityString(R.plurals.list_client_selected, count, count)
        }
    }

    private fun showMessageClientsDeleted(count: Int) {
        Snackbar.make(listView,
            getString(R.string.message_clients_deleted, count),
            Snackbar.LENGTH_LONG
            )
            .setAction(R.string.undo){
                viewModel.undoDelete()
            }
            .show()
    }

    fun showClients(clients: List<Client>){
        val adapter = ClientAdapter(requireContext(), clients)
        listAdapter = adapter
    }

    fun search(text: String = ""){
        viewModel.search(text)
    }

    fun navigateToClientFormFrament(){
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_fragmentListClient_to_clientFormFragment)
    }

    fun navigateToProductListFragment() {
        val navController = requireActivity().findNavController(R.id.navHostFragment)
        navController.popBackStack(R.id.fragmentListProduct, false)
        navController.navigate(R.id.fragmentListProduct)
    }

    interface OnClientClickListener{
        fun onClientClick(client: Client)
    }

}