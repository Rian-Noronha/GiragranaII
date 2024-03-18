package com.rn.giragrana.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentTabsBinding

class TabsFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var binding: FragmentTabsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabsBinding.inflate(inflater, container, false)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        setupViewPager(viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }


    private fun setupViewPager(viewPager: ViewPager){
        val adapter = TabsPagerAdapter(childFragmentManager)
        adapter.addFragment(TotalInvestedFragment(), "Total Investido")
        adapter.addFragment(TotalResalesFragment(), "Total Revendido")
        adapter.addFragment(TotalProfitFrament(), "Total Lucro")
        viewPager.adapter = adapter
    }
}