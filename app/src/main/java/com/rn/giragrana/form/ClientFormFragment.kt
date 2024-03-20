package com.rn.giragrana.form
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentClientFormBinding
import com.rn.giragrana.model.Client
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClientFormFragment : DialogFragment(){
    private val viewModel: ClientFormViewModel by viewModel()
    private var client: Client? = null
    private lateinit var binding: FragmentClientFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClientFormBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run{
            val clientId = arguments?.getLong(EXTRA_CLIENT_ID, 0)?: 0
            if(clientId > 0){
                viewModel.loadClient(clientId).observe(viewLifecycleOwner) { loadedClient ->
                    client = loadedClient
                    showClient(loadedClient)
                }

            }

            binding.edtContact.setOnEditorActionListener{_, i, _ ->
                handleKeyboardEvent(i)
            }

            dialog?.setTitle(R.string.action_new_client)
            dialog?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
            )
        }
    }

    private fun showClient(client:Client){
        binding.edtName.setText(client.name)
        binding.edtContact.setText(client.contact)
    }

    private fun errorClientInvalid(){
        Toast.makeText(requireContext(), R.string.error_invalid_client, Toast.LENGTH_SHORT).show()
    }

    private fun errorSaveClient() {
        Toast.makeText(requireContext(), R.string.error_client_not_found, Toast.LENGTH_SHORT).show()
    }

    private fun handleKeyboardEvent(actionId: Int): Boolean{
        if(EditorInfo.IME_ACTION_DONE == actionId){
            saveClient()
            return true
        }
        return false

    }

    private fun saveClient(){
        try{
           arguments?.run{
                val client = client ?: Client()
                val clientId = arguments?.getLong(EXTRA_CLIENT_ID, 0)?:0
                client.id = clientId
                client.name = binding.edtName.text.toString()
                client.contact = binding.edtContact.text.toString()

                if(viewModel.saveClient(client)){
                    dialog?.dismiss()
                    navigateToClientListFragment()
                }else{
                    errorClientInvalid()
                }

            }
        }catch(e: Exception){
            errorSaveClient()
        }
    }

    private fun navigateToClientListFragment(){
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_clientFormFragment_to_fragmentListClient)
    }

    companion object{
        private const val EXTRA_CLIENT_ID = "clientId"
    }

}

