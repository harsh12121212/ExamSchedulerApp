package com.app.examschedulerapp

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.data.*
import com.app.examschedulerapp.databinding.AdminDashboardBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminDashboardFragment : Fragment() {

    private lateinit var binding: AdminDashboardBinding
    private lateinit var user: FirebaseAuth
    var list: ArrayList<examdata> = ArrayList()

    lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AdminDashboardBinding.inflate(inflater, container, false)
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
                        Log.e("TAG", "onDataChange: " + list.size)
                        userAdapter=UserAdapter(this@AdminDashboardFragment,list)
                binding.rvData.layoutManager=LinearLayoutManager(activity)
                binding.rvData.adapter=userAdapter
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
//Actionbar menu code starts here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
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
//actionbar menu code ends here
    private fun showSnackBar(response: String) {
        val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}