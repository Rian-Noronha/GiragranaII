package com.rn.giragrana.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentResaleFormBinding
import com.rn.giragrana.list.ClientListViewModel
import com.rn.giragrana.list.ProductListViewModel
import com.rn.giragrana.model.Resale
import com.rn.giragrana.utils.DateUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
class ResaleFormFragment : DialogFragment() {
    private val viewModel: ResaleFormViewModel by viewModel()
    private val viewModelProduct: ProductListViewModel by viewModel()
    private val viewModelClient: ClientListViewModel by viewModel()
    private var resale: Resale? = null
    private lateinit var binding: FragmentResaleFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResaleFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run{
            val resaleId =  arguments?.getLong(EXTRA_RESALE_ID, 0)?:0
            if(resaleId > 0){
                viewModel.loadResale(resaleId).observe(viewLifecycleOwner, Observer { loadedResale ->
                    resale = loadedResale
                    showResale(loadedResale)
                })
            }
        }

        binding.seekBarProfit.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.txtProfitPercentage.text = getString(R.string.form_label_profit_percentage, progress)

                arguments?.run{
                    if(binding.spinnerProduct.selectedItemPosition != -1){
                        val selectedProductPosition = binding.spinnerProduct.selectedItemPosition
                        val selectedProduct = viewModelProduct.getProducts()?.value?.get(selectedProductPosition)
                        val productId = selectedProduct?.id ?: 0
                        binding.edtProductPrice.setText(selectedProduct?.price.toString())

                        val productPrice = selectedProduct?.price
                        val profitPercentage = binding.seekBarProfit.progress.toFloat()

                        binding.edtResalePrice.setText(calculateResalePrice(productPrice, profitPercentage).toString())
                    }else{
                        Toast.makeText(requireContext(), R.string.error_resales, Toast.LENGTH_LONG).show()
                    }

                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        viewModelProduct.getProducts()?.observe(viewLifecycleOwner, Observer { products ->
            if (products != null) {
                val productAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, products)
                productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerProduct.adapter = productAdapter
            }
        })


        viewModelClient.getClients()?.observe(viewLifecycleOwner, Observer { clients ->
            if(clients != null){
                val clientAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clients)
                clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerClient.adapter = clientAdapter
            }
        })


        if (viewModelProduct.getProducts()?.value == null) {
            searchProducts()
        }

        if(viewModelClient.getClients()?.value == null){
            searchClientes()
        }

        binding.edtPaymentMethod.setOnEditorActionListener{_, i, _ ->
            handleKeyboardEvent(i)
        }

        dialog?.setTitle(R.string.action_new_client)
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )


    }

    private fun showResale(resale: Resale){
        binding.edtResalePrice.setText(resale.resalePrice.toString())
        binding.edtReceivingDate.setText(resale.receivingDate)
        binding.edtPaymentMethod.setText(resale.paymentMethod)
    }

    fun searchProducts(text: String = "") {
        viewModelProduct.search(text)
    }

    fun searchClientes(text: String = "") {
        viewModelClient.search(text)
    }

    private fun errorResaleInvalid(){
        Toast.makeText(requireContext(), R.string.error_invalid_resale, Toast.LENGTH_SHORT).show()
    }

    private fun errorSaveResale() {
        Toast.makeText(requireContext(), R.string.error_resale_not_found, Toast.LENGTH_SHORT).show()
    }

    private fun handleKeyboardEvent(actionId: Int): Boolean{
        if(EditorInfo.IME_ACTION_DONE == actionId){
            saveResale()
            return true
        }
        return false

    }


    private fun saveResale(){
        try{
            arguments?.run{
                val resale = resale ?: Resale()
                val resaleId = arguments?.getLong(EXTRA_RESALE_ID, 0)?:0
                resale.id = resaleId

                val selectedProductPosition = binding.spinnerProduct.selectedItemPosition
                val selectedProduct = viewModelProduct.getProducts()?.value?.get(selectedProductPosition)
                val productId = selectedProduct?.id ?: 0

                val selectedClientPosition = binding.spinnerClient.selectedItemPosition
                val selectedClient = viewModelClient.getClients()?.value?.get(selectedClientPosition)
                val clientId = selectedClient?.id ?: 0

                val receivingDate = binding.edtReceivingDate.text.toString()
                val paymentMethod = binding.edtPaymentMethod.text.toString()

                resale.productId = productId
                resale.clientId = clientId
                resale.resalePrice = binding.edtResalePrice.text.toString().toFloat()
                resale.date = DateUtils.getCurrentFormattedDate()
                resale.receivingDate = receivingDate
                resale.paymentMethod = paymentMethod

                if(viewModel.saveResale(resale)){
                    dialog?.dismiss()
                    navigateToResaleListFrament()
                }else{
                    errorResaleInvalid()
                }


            }
        }catch(e: Exception){
            errorSaveResale()
        }
    }

    private fun calculateResalePrice(originalPrice: Float?, profitPercentage: Float): Float {
        val profitMultiplier = 1 + profitPercentage / 100
        return (originalPrice?.times(profitMultiplier) ?: 0) as Float
    }

    fun navigateToResaleListFrament(){
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_resaleFormFragment_to_resaleListFragment)
    }


    companion object{
        private const val EXTRA_RESALE_ID = "resaleId"
    }
}