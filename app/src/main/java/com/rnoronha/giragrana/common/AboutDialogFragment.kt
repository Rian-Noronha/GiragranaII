package com.rnoronha.giragrana.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.rnoronha.giragrana.R
class AboutDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener { _, i ->
            if (i == DialogInterface.BUTTON_NEGATIVE) {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/Rian-Noronha"));
                startActivity(intent)
            }
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.about_title)
            .setMessage(R.string.about_message)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(R.string.about_button_site, listener)
            .create()
    }
}