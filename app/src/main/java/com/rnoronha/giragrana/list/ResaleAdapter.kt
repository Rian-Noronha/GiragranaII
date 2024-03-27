package com.rnoronha.giragrana.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rnoronha.giragrana.databinding.ItemResaleBinding
import com.rnoronha.giragrana.model.Client
import com.rnoronha.giragrana.model.Product
import com.rnoronha.giragrana.model.Resale
import com.rnoronha.giragrana.utils.PriceUtils

class ResaleAdapter(
    context: Context,
    resales: List<Resale>,
    private val productsMap: Map<Long, Product>,
    private val clientsMap: Map<Long, Client>
) : ArrayAdapter<Resale>(context, 0, resales){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemResaleBinding
        val resale = getItem(position)
        if(convertView == null){
            binding = ItemResaleBinding.inflate(LayoutInflater.from(context), parent, false)
        }else{
            binding = ItemResaleBinding.bind(convertView)
        }

        val viewHolder = ViewHolder(binding)
        val productId = resale?.productId ?: 0
        val product = productsMap[productId]

        val clientId = resale?.clientId ?: 0
        val client = clientsMap[clientId]

        viewHolder.txtProductName.text = product?.name
        viewHolder.txtProductPrice.text = product?.price?.let { PriceUtils.formatPrice(it) }
        viewHolder.txtPriceResale.text = resale?.let { PriceUtils.formatPrice(it.resalePrice) }
        viewHolder.txtClientName.text = client?.name
        viewHolder.txtClientContact.text = client?.contact
        viewHolder.txtDate.text = resale?.date
        viewHolder.txtReceivingDate.text = resale?.receivingDate
        viewHolder.txtPaymentMethod.text = resale?.paymentMethod

        return binding.root

    }

    class ViewHolder(val binding: ItemResaleBinding){
        val txtProductName: TextView = binding.txtProductName
        val txtProductPrice: TextView = binding.txtProductPrice
        val txtPriceResale: TextView = binding.txtPriceResale
        val txtClientName: TextView = binding.txtClientName
        val txtClientContact: TextView = binding.txtClientContact
        val txtDate: TextView = binding.txtDate
        val txtReceivingDate: TextView = binding.txtReceivingDate
        val txtPaymentMethod: TextView = binding.txtPaymentMethod
    }
}