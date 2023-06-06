package com.app.examschedulerapp

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.adapters.AdminAdapter
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

    lateinit var AdminAdapter: AdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAdminPendingrequestBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        user = FirebaseAuth.getInstance()
        AdminAdapter = AdminAdapter(this, list) // Initialize the adapter
        binding.rvData.layoutManager = LinearLayoutManager(activity)
        binding.rvData.adapter = AdminAdapter
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
                            AdminAdapter = AdminAdapter(this@AdminPendingRequestsFragment, list)
                            binding.rvData.layoutManager = LinearLayoutManager(activity)
                            binding.rvData.adapter = AdminAdapter
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

//override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//    inflater.inflate(R.menu.main_menu, menu)
//}
//
//override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    when (item.itemId) {
//        R.id.logout -> {
//            val dialogClickListener =
//                DialogInterface.OnClickListener { dialog, which ->
//                    when (which) {
//                        DialogInterface.BUTTON_POSITIVE -> {
//                            user.signOut()
//                            showSnackBar("Successfully Logging out! ")
//                            findNavController().navigate(R.id.action_adminFragment_to_loginFragment)
//                        }
//                        DialogInterface.BUTTON_NEGATIVE -> {
//                            dialog.dismiss()
//                        }
//                    }
//                }
//            val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
//            builder.setMessage("Do you want to Logout?")
//                .setPositiveButton("Yes", dialogClickListener)
//                .setNegativeButton("No", dialogClickListener)
//                .show()
//        }
//    }
//    return true
//}
