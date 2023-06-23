package com.app.examschedulerapp.Admin.adminView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.adapters.AdminAllRequestsAdapter
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.FragmentAdminAllRequestsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminAllRequestsFragment : Fragment() {

    private lateinit var binding: FragmentAdminAllRequestsBinding
    private lateinit var user: FirebaseAuth
    var list: ArrayList<examdata> = ArrayList()

    lateinit var AdminAllRequestsAdapter: AdminAllRequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminAllRequestsBinding.inflate(inflater, container, false)

        user = FirebaseAuth.getInstance()
        AdminAllRequestsAdapter = AdminAllRequestsAdapter(this, list) // Initialize the adapter
        binding.adminallreqRv.layoutManager = LinearLayoutManager(activity)
        binding.adminallreqRv.adapter = AdminAllRequestsAdapter
        retrieveDataFromDatabase()

        return binding.root
    }

    private fun retrieveDataFromDatabase() {
        binding.progressbar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference(DBConstants.APPLICATION)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    binding.progressbar.visibility = View.GONE
                    try {
                        list.clear()
                        p0.children.forEach { it1 ->
                            it1.getValue(examdata::class.java)?.let {
                                list.add(it)
                            }
                            AdminAllRequestsAdapter =
                                AdminAllRequestsAdapter(this@AdminAllRequestsFragment, list)
                            binding.adminallreqRv.layoutManager = LinearLayoutManager(activity)
                            binding.adminallreqRv.adapter = AdminAllRequestsAdapter
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {}
            })
    }

}