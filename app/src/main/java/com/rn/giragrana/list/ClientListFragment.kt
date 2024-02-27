package com.rn.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rn.giragrana.databinding.FragmentListClientBinding

class ClientListFragment : Fragment(){
    private lateinit var binding: FragmentListClientBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListClientBinding.inflate(inflater, container, false)
        return binding.root
    }


}