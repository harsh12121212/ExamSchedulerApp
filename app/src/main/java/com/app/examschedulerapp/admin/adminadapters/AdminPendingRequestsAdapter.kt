package com.app.examschedulerapp.admin.adminadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.admin.adminView.AdminPendingRequestsFragment
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.Examdata
import com.app.examschedulerapp.databinding.CvAdminPendingrequestsBinding
import com.app.examschedulerapp.repository.ExamRepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminPendingRequestsAdapter(
    var context: AdminPendingRequestsFragment,
    var list: List<Examdata>,
    private val examRepository: ExamRepositoryInterface
) : RecyclerView.Adapter<AdminPendingRequestsAdapter.UserViewHolder>() {

    private var currentUserCentre: String? = null

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val usersRef = FirebaseDatabase.getInstance().reference.child(DBConstants.USERS)
        currentUser?.uid?.let { uid ->
            usersRef.child(uid).child("centre").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    currentUserCentre = dataSnapshot.value as? String
                    notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle onCancelled if needed
                }
            })
        }
    }

    inner class UserViewHolder(val adapterBinding: CvAdminPendingrequestsBinding) :
        RecyclerView.ViewHolder(adapterBinding.root) {
        init {
            adapterBinding.acceptbutton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = list[position]
                    examRepository.updateStatus(currentItem, "Accepted") { success ->
                        if (success) {
                            context.showSnackBar("The request is accepted")
                        } else {
                            context.showSnackBar("Failed to update status")
                        }
                    }
                }
            }

            adapterBinding.declinebutton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = list[position]
                    examRepository.updateStatus(currentItem, "Declined") { success ->
                        if (success) {
                            context.showSnackBar("The request is declined")
                        } else {
                            context.showSnackBar("Failed to update status")
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CvAdminPendingrequestsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = list[position]
        val binding = holder.adapterBinding

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

            val acceptButton = binding.acceptbutton
            val declineButton = binding.declinebutton

            binding.cvStudname.text = currentItem.studentName
            binding.cvStudemail.text = currentItem.studentEmailId
            binding.dtCity.text = "Selected City : " + currentItem.sf_city
            binding.dtCentre.text = "Selected Center : " + currentItem.sf_centre
            binding.dtSlot.text = "Selected Slot : " + currentItem.sf_slot

            if (currentItem.status == "Accepted" || currentItem.status == "Declined") {
                binding.root.visibility = View.GONE
                binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            } else {
                binding.root.visibility = View.VISIBLE
            }

            acceptButton.setOnClickListener {
                examRepository.updateStatus(currentItem, "Accepted") { success ->
                    if (success) {
                        context.showSnackBar("The request is accepted")
                    } else {
                        context.showSnackBar("Failed to update status")
                    }
                }
            }

            declineButton.setOnClickListener {
                examRepository.updateStatus(currentItem, "Declined") { success ->
                    if (success) {
                        context.showSnackBar("The request is declined")
                    } else {
                        context.showSnackBar("Failed to update status")
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}