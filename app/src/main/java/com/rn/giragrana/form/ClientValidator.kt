package com.rn.giragrana.form

import com.rn.giragrana.model.Client

class ClientValidator {
    fun validate(info: Client) = with(info){
        checkName(name) && checkContact(contact)
    }

    private fun checkName(name: String) = name.length in 2..40
    private fun checkContact(contact: String) = contact.length in 5..50
}