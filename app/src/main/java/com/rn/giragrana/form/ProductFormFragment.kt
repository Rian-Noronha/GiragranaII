package com.rn.giragrana.form
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentProductFormBinding
import com.rn.giragrana.model.Product
import com.rn.giragrana.utils.PriceUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
class ProductFormFragment : DialogFragment() {

    private val viewModel: ProductFormViewModel by viewModel()
    private var product: Product? = null
    private lateinit var binding: FragmentProductFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            val productId = arguments?.getLong(EXTRA_PRODUCT_ID, 0) ?: 0
            if(productId > 0){
                viewModel.loadProduct(productId).observe(viewLifecycleOwner, Observer { loadedProduct ->
                    product = loadedProduct
                    showProduct(loadedProduct)
                })
            }

            binding.edtPrice.setOnEditorActionListener { _, i, _ ->
                handleKeyboardEvent(i)
            }
            dialog?.setTitle(R.string.action_new_product)

            dialog?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
            )
        }
    }


    private fun showProduct(product: Product){
        binding.edtName.setText(product.name)
        binding.edtDescription.setText(product.description)
        binding.edtPrice.setText(PriceUtils.formatPrice(product.price))
        binding.switchSold.isChecked = product.sold
    }

    private fun errorProductInvalid() {
        Toast.makeText(requireContext(), R.string.error_invalid_product, Toast.LENGTH_SHORT).show()
    }

    private fun errorSaveProduct() {
        Toast.makeText(requireContext(), R.string.error_product_not_found, Toast.LENGTH_SHORT).show()
    }

    private fun handleKeyboardEvent(actionId: Int): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            saveProduct()
            return true
        }
        return false
    }

    private fun saveProduct(){
        try{
            arguments?.run {
                val product = product ?: Product()
                val productId = arguments?.getLong(EXTRA_PRODUCT_ID, 0)?: 0
                product.id = productId
                product.name = binding.edtName.text.toString()
                product.description = binding.edtDescription.text.toString()
                product.price = binding.edtPrice.text.toString().toFloat()
                product.sold = binding.switchSold.isChecked

                if(viewModel.saveProduct(product)){
                    dialog?.dismiss()
                    navigateToProductListFragment()
                }else{
                    errorSaveProduct()
                }
            }

        }catch (e: Exception){
            errorProductInvalid()
        }
    }

    private fun navigateToProductListFragment(){
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_productFormFragment_to_fragmentListProduct)
    }

    companion object{
        private const val EXTRA_PRODUCT_ID = "productId"
    }





}