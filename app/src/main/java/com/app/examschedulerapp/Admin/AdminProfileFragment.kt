package com.app.examschedulerapp.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.examschedulerapp.data.admin
import com.app.examschedulerapp.databinding.FragmentAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminProfileFragment : Fragment() {

    private lateinit var binding: FragmentAdminProfileBinding
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance().reference
        fun currentUserReference(): DatabaseReference = database.child("Users").child(auth.currentUser!!.uid)
        currentUserReference().addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){
                val admin=dataSnapshot.getValue(admin::class.java)

                binding.tvAdminprofileAdminname.text = "Name : "+ admin!!.name
                binding.tvAdminprofileAdminemail.text ="Email : "+ admin!!.email
                binding.tvAdminprofileAdmincity.text ="City of the center: "+ admin.city
                binding.tvAdminprofileAdmincenter.text ="Centre name : "+ admin!!.centre

            }
            override fun onCancelled(error: DatabaseError){
            }
        })

        return binding.root
    }
}