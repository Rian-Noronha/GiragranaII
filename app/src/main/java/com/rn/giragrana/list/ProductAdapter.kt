package com.rn.giragrana.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rn.giragrana.databinding.ItemProductBinding
import com.rn.giragrana.model.Product

class ProductAdapter(context: Context, products: List<Product>):
    ArrayAdapter<Product>(context, 0, products){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemProductBinding
        val product = getItem(position)
        if(convertView == null){
            binding = ItemProductBinding.inflate(LayoutInflater.from(context), parent, false)
        }else{
            binding = ItemProductBinding.bind(convertView)
        }

        val viewHolder = ViewHolder(binding)
        viewHolder.txtImage.text = product?.image
        if(product?.sold == true){
            viewHolder.txtSold.text = "Tá vendido"
        }else{
            viewHolder.txtSold.text = "Tá não"
        }

        return binding.root
    }

    class ViewHolder(val binding: ItemProductBinding){
        val txtImage: TextView = binding.txtImage
        val txtSold: TextView = binding.txtSold
    }

}