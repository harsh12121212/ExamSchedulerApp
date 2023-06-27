package com.app.examschedulerapp.Admin.adminView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.Admin.adminAdapters.AdminPendingRequestsAdapter
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.FragmentAdminPendingrequestBinding
import com.app.examschedulerapp.repository.ExamRepository
import com.app.examschedulerapp.repository.ExamRepositoryInterface
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminPendingRequestsFragment : Fragment() {

    private lateinit var binding: FragmentAdminPendingrequestBinding
    private lateinit var examRepository: ExamRepositoryInterface
    private var list: ArrayList<examdata> = ArrayList()
    private lateinit var adminPendingRequestsAdapter: AdminPendingRequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminPendingrequestBinding.inflate(inflater, container, false)

        examRepository = ExamRepository()
        adminPendingRequestsAdapter = AdminPendingRequestsAdapter(this, list, examRepository)
        binding.rvData.layoutManager = LinearLayoutManager(activity)
        binding.rvData.adapter = adminPendingRequestsAdapter
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
                                Log.e("TAG", "onDataChange: $it")
                            }
                            Log.e("TAG", "onDataChange: ${list.size}")
                            adminPendingRequestsAdapter.notifyDataSetChanged()
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
