package com.app.examschedulerapp.admin.adminadapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.admin.adminView.AdminAllRequestsFragment
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.Examdata
import com.app.examschedulerapp.databinding.CvAdminAllrequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminAllRequestsAdapter(var context: AdminAllRequestsFragment) :
    RecyclerView.Adapter<AdminAllRequestsAdapter.UserViewHolder>() {
    private var exams: List<Examdata> = emptyList()
    private var currentUserCentre: String? = null

    init {
        // Fetch the current user's centre from the Users table
        val currentUser = FirebaseAuth.getInstance().currentUser
        val usersRef = FirebaseDatabase.getInstance().reference.child(DBConstants.USERS)
        currentUser?.uid?.let { uid ->
            usersRef.child(uid).child("centre").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    currentUserCentre = dataSnapshot.value as? String
                    notifyDataSetChanged() // Refresh the adapter after getting the user's centre
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    inner class UserViewHolder(val adapterBinding: CvAdminAllrequestBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CvAdminAllrequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = exams[position]
        val binding = holder.adapterBinding

        Log.d("AdminChecking", "Inside the if $currentUserCentre")
        Log.d("AdminChecking", "Inside the if ${currentItem.sf_centre}")

        // Check if the centre in the current item matches the current user's centre
        if (currentItem.sf_centre != currentUserCentre) {
            binding.root.visibility = View.GONE
            binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            return
        } else {
            binding.root.visibility = View.VISIBLE
            binding.root.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        binding.cvStudname.text = currentItem.studentName
        binding.cvStudemail.text = currentItem.studentEmailId
        binding.adminallreqTvCity.text = "Selected City: ${currentItem.sf_city}"
        binding.adminallreqTvCentre.text = "Selected Center: ${currentItem.sf_centre}"
        binding.adminallreqTvSlot.text = "Selected Slot: ${currentItem.sf_slot}"
        binding.adminallreqTvStatus.text = currentItem.status
    }

    override fun getItemCount(): Int {
        return exams.size
    }

    fun setExams(exams: List<Examdata>) {
        this.exams = exams
        notifyDataSetChanged()
    }
}