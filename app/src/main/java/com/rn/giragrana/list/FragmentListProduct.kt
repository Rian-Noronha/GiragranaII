package com.rn.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentListProductBinding


class FragmentListProduct : Fragment() {

    private lateinit var binding: FragmentListProductBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListProductBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_client ->
                Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(R.id.action_fragmentListProduct_to_fragmentListClient)
        }
        return super.onOptionsItemSelected(item)
    }

}