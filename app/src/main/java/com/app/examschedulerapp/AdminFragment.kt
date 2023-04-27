package com.app.examschedulerapp

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.examschedulerapp.data.Centre
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.data.student
import com.app.examschedulerapp.databinding.FragmentAdminBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private lateinit var user: FirebaseAuth
    var list: ArrayList<examdata> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        user = FirebaseAuth.getInstance()
        retrieveDataFromDatabase()

        return binding.root
    }

    fun retrieveDataFromDatabase() {


        binding.progressbar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference(
            DBConstants.APPLICATION
        ).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                binding.progressbar.visibility = View.GONE
                try {
                    list.clear()
                    p0.children.forEach { it1 ->
                        it1.getValue(examdata::class.java)?.let {
                            list.add(it)
                            Log.e("TAG", "onDataChange: " + it.toString())
                        }
//                        it1.children.forEach {
//                            Log.e("TAG", "onDataChange: "+it )
//                        }

                    }
                    showSnackBar("data loaded")
                    // TODO: setups recyclver view here with list data
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
            }
            R.id.logout -> {
                val dialogClickListener =
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                user.signOut()
                                showSnackBar("Successfully Logging out! ")
                                findNavController().navigate(R.id.action_adminFragment_to_loginFragment)
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                                dialog.dismiss()
                            }
                        }
                    }
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show()
            }
        }
        return true
    }

    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}