package com.app.examschedulerapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.examschedulerapp.adapters.StudentStatusAdapter
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
        setHasOptionsMenu(true)
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
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

   fun showSnackBar(response: String) {
    val snackbar = Snackbar.make(binding.root, response, Snackbar.LENGTH_LONG)
    snackbar.show()
    }
}
