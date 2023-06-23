package com.app.examschedulerapp.Admin.adminView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.adapters.AdminPendingRequestsAdapter
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.FragmentAdminPendingrequestBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminPendingRequestsFragment : Fragment() {

    private lateinit var binding: FragmentAdminPendingrequestBinding
    private lateinit var user: FirebaseAuth
    var list: ArrayList<examdata> = ArrayList()

    lateinit var AdminPendingRequestsAdapter: AdminPendingRequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAdminPendingrequestBinding.inflate(inflater, container, false)

        user = FirebaseAuth.getInstance()
        AdminPendingRequestsAdapter = AdminPendingRequestsAdapter(this, list) // Initialize the adapter
        binding.rvData.layoutManager = LinearLayoutManager(activity)
        binding.rvData.adapter = AdminPendingRequestsAdapter
        retrieveDataFromDatabase()

        return binding.root
    }

    fun retrieveDataFromDatabase() {

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
                                Log.e("TAG", "onDataChange: " + it.toString())
                            }
                            Log.e("TAG", "onDataChange: " + list.size)
                            AdminPendingRequestsAdapter = AdminPendingRequestsAdapter(this@AdminPendingRequestsFragment, list)
                            binding.rvData.layoutManager = LinearLayoutManager(activity)
                            binding.rvData.adapter = AdminPendingRequestsAdapter
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }

    fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}
