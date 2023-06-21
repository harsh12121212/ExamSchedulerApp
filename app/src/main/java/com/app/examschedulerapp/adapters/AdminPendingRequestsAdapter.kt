package com.app.examschedulerapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.Admin.adminView.AdminPendingRequestsFragment
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.Student.studentModel.examdata
import com.app.examschedulerapp.databinding.CvAdminPendingrequestsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminPendingRequestsAdapter(
    var context: AdminPendingRequestsFragment,
    var list: ArrayList<examdata>
) : RecyclerView.Adapter<AdminPendingRequestsAdapter.UserViewHolder>() {

    private var currentUserCentre: String? = null

    init {
        // Fetch the current user's centre from the Users table
        val currentUser = FirebaseAuth.getInstance().currentUser
        val usersRef = FirebaseDatabase.getInstance().reference.child(DBConstants.USERS)
        currentUser?.uid?.let { uid ->
            usersRef.child(uid).child("centre")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        currentUserCentre = dataSnapshot.value as? String
                        notifyDataSetChanged() // Refresh the adapter after getting the user's centre
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
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
                    updateStatus(currentItem, "Accepted") { success ->
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
                    updateStatus(currentItem, "Declined") { success ->
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
        val binding = CvAdminPendingrequestsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = list[position]
        val binding = holder.adapterBinding

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

            // Set click listener for the accept button
            acceptButton.setOnClickListener {
                updateStatus(currentItem, "Accepted") { success ->
                    if (success) {
                        context.showSnackBar("The request is accepted")
                    } else {
                        context.showSnackBar("Failed to update status")
                    }
                }
            }

            // Set click listener for the decline button
            declineButton.setOnClickListener {
                updateStatus(currentItem, "Declined") { success ->
                    if (success) {
                        context.showSnackBar("The request is declined")
                    } else {
                        context.showSnackBar("Failed to update status")
                    }
                }
            }
        }
    }

    private fun updateStatus(item: examdata, status: String, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference.child(DBConstants.APPLICATION)

        val query = database.orderByChild("sf_centre").equalTo(item.sf_centre)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var success = false

                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key // Retrieve the key of the matching data
                    val data = snapshot.getValue(examdata::class.java)

                    // Check if the retrieved data matches the item
                    if (data != null && data == item) {
                        val updateData = HashMap<String, Any>()
                        updateData["status"] = status //updating the status
                        if (status == "Declined") {
                            updateData["countid"] = 0//updating the countid to 0 if declined
                        }

                        database.child(key!!).updateChildren(updateData)
                            .addOnSuccessListener {
                                success = true
                                callback.invoke(success)
                            }
                            .addOnFailureListener {
                                callback.invoke(success)
                                context.showSnackBar("Failed to update status")
                            }

                        break // Exit the loop after updating the status for the item
                    }
                }

                callback.invoke(success) // Invoke the callback after the loop
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}