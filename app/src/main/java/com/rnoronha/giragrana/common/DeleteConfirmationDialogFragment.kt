package com.rnoronha.giragrana.common

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.rnoronha.giragrana.R

class DeleteConfirmationDialogFragment : DialogFragment() {
    private var confirmationListener: ConfirmationListener? = null

    fun setConfirmationListener(listener: ConfirmationListener) {
        this.confirmationListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.interrogation_delete_account)
                .setPositiveButton(R.string.interrogation_delete_account_positive) { _, _ ->
                    confirmationListener?.onConfirmDelete()
                }
                .setNegativeButton(R.string.interrogation_delete_account_negative) { _, _ ->
                    confirmationListener?.onCancelDelete()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface ConfirmationListener {
        fun onConfirmDelete()
        fun onCancelDelete()
    }
}