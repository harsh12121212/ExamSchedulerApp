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
import com.app.examschedulerapp.databinding.FragmentStudentStatusBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class StudentStatusFragment : Fragment() {

    lateinit var binding: FragmentStudentStatusBinding
    private lateinit var user: FirebaseAuth
    var list: ArrayList<examdata> = ArrayList()
    lateinit var userAdapter: StudentStatusAdapter
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentStatusBinding.inflate(inflater, container, false)

        displaydata()

        return binding.root
    }

    private fun displaydata(){
        databaseReference = FirebaseDatabase.getInstance().getReference(DBConstants.APPLICATION)
        // Specifying path and filter category and adding a listener
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference.orderByChild("studentId").equalTo(currentuser).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    list.clear()
                    p0.children.forEach { it1 ->
                        it1.getValue(examdata::class.java)?.let {
                            list.add(it)
                            Log.e("TAG", "onDataChange: " + it.toString())
                        }
                        Log.e("TAG", "onDataChange: " + list.size)
                        userAdapter = StudentStatusAdapter(this@StudentStatusFragment, list)
                        binding.rvData.layoutManager = LinearLayoutManager(activity)
                        binding.rvData.adapter = userAdapter
                        showSnackBar("data loaded")
                    }
                } else{
                    showSnackBar("Data is not found")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    //action bar menu code starts here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
            }
            R.id.logout -> {
                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            user.signOut()
                            showSnackBar("Successfully Logging out! ")
                            findNavController().navigate(R.id.action_firstFragment_to_loginFragment)
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                builder.setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show()
            }
        }
        return true
    }
//action bar menu code ends here
    private fun showSnackBar(response: String) {
    val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
    snackbar.show()
    }
}
