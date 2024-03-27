package com.rnoronha.giragrana.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.rnoronha.giragrana.R
import com.rnoronha.giragrana.common.DeleteConfirmationDialogFragment
import com.rnoronha.giragrana.databinding.FragmentDeleteAccountBinding
import org.koin.android.ext.android.inject

class DeleteAccountFragment :
    Fragment(),
    DeleteConfirmationDialogFragment.ConfirmationListener {
    private lateinit var binding: FragmentDeleteAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var deleteAccountViewModel: DeleteAccountViewModel
    private val factory: DeleteAccountViewModelFactory by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        deleteAccountViewModel =
            ViewModelProvider(requireActivity(), factory).get(DeleteAccountViewModel::class.java)
        binding.btnDeleteAccount.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val confirmationDialog = DeleteConfirmationDialogFragment()
        confirmationDialog.setConfirmationListener(this)
        confirmationDialog.show(childFragmentManager, "ConfirmationDialog")
    }


    private fun deleteAccount() {
        Log.d("DeleteAccountFragment", "deleteAccount() called")
        val user = auth.currentUser
        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    deleteAccountViewModel.deleteAllClientsAndProducts()
                    navigateToLogin()
                } else {
                    val exception = task.exception
                    if (exception != null) {
                        Log.d(
                            "DeleteAccountFragment",
                            "Exception in DeleteAccount: ${exception.message}"
                        )
                        Toast.makeText(
                            requireContext(),
                            R.string.error_delete_account,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    fun navigateToLogin() {
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_deleteAccountFragment_to_fragmentLogin)
    }

    override fun onConfirmDelete() {
        Log.d("DeleteAccountFragment", "onConfirmDelete() called")
        this.deleteAccount()
    }

    override fun onCancelDelete() {
        Toast.makeText(requireContext(), R.string.stay_here, Toast.LENGTH_SHORT).show()
    }


}