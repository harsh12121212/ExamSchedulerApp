package com.app.examschedulerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.app.examschedulerapp.adapters.AdminDashboardPageAdapter
import com.app.examschedulerapp.databinding.FragmentAdminDashboardBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AdminDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAdminDashboardBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: AdminDashboardPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        // ViewPager for admin Dashboard received
        tabLayout = binding.admindashboardTabLayout
        viewPager2 = binding.admindashboardViewpager2

        // Creating the adapter
        adapter = AdminDashboardPageAdapter((activity as FragmentActivity).supportFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        viewPager2.adapter = adapter // Setting the adapter to the ViewPager2

        // Set up TabLayout with ViewPager2 using TabLayoutMediator
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Pending"
                1 -> tab.text = "All"
            }
        }.attach()
        return binding.root
    }
}