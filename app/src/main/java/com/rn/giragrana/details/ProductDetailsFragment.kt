package com.rn.giragrana.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentProductDetailsBinding
import com.rn.giragrana.model.Product
import com.rn.giragrana.utils.PriceUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
class ProductDetailsFragment : Fragment(){
    private val viewModel: ProductDetailsViewModel by viewModel()
    private lateinit var binding: FragmentProductDetailsBinding
    private var product: Product? = null
    private var shareActionProvider: ShareActionProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            val id = getLong(EXTRA_PRODUCT_ID, -1)
            viewModel.loadProductDetails(id).observe(viewLifecycleOwner, Observer { product ->
                if (product != null) {
                    showProductDetails(product)
                } else {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.remove(requireParentFragment())
                        ?.commit()
                    errorProductNotFound()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.product_details, menu)
        val shareItem = menu?.findItem(R.id.action_share)
        shareActionProvider = shareItem?.let { MenuItemCompat.getActionProvider(it) } as? ShareActionProvider
        setShareIntent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.action_edit_product){
            arguments?.run {
                val id = getLong(EXTRA_PRODUCT_ID, -1)

                val action = ProductDetailsFragmentDirections
                    .actionProductDetailsFragmentToProductFormFragment(productId = id)

                findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setShareIntent(){
        arguments?.run {
            val id = getLong(EXTRA_PRODUCT_ID, -1)
            viewModel.loadProductDetails(id).observe(viewLifecycleOwner, Observer { productLoaded ->
                val text = getString(R.string.share_text, productLoaded?.name, productLoaded?.description)
                shareActionProvider?.setShareIntent(Intent(Intent.ACTION_SEND).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                })
            })
        }

    }

    private fun showProductDetails(product: Product){
        this.product = product
        binding.txtName.text = product.name
        binding.txtPrice.text = PriceUtils.formatPrice(product.price)
        binding.txtDesciption.text = product.description
        binding.txtImage.text = product.image
    }


    private fun errorProductNotFound(){
        binding.txtName.text = getString(R.string.error_product_not_found)
        binding.txtDesciption.visibility = View.GONE
        binding.txtImage.visibility = View.GONE
        binding.txtPrice.visibility = View.GONE
    }


    companion object{
        private const val EXTRA_PRODUCT_ID = "productId"

    }
}