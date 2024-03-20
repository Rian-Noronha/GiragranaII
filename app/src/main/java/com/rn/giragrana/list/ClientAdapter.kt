package com.rn.giragrana.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rn.giragrana.databinding.ItemClientBinding
import com.rn.giragrana.model.Client

class ClientAdapter(
    context: Context, clients: List<Client>
) : ArrayAdapter<Client>(context, 0, clients) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val client = getItem(position)
        val binding: ItemClientBinding = if (convertView == null) {
            ItemClientBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemClientBinding.bind(convertView)
        }

        val viewHolder = ViewHolder(binding)
        viewHolder.txtName.text = client?.name
        viewHolder.txtContact.text = client?.contact

        return binding.root
    }


    class ViewHolder(val binding: ItemClientBinding) {
        val txtName: TextView = binding.txtName
        val txtContact: TextView = binding.txtContact
    }

}