package com.app.examschedulerapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.AdminPendingRequestsFragment
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.CvAdminpendingrequestBinding
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

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
        }
    }

    inner class UserViewHolder(val adapterBinding: CvAdminpendingrequestBinding) :
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
        val binding =
            CvAdminpendingrequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = list[position]

        // Check if the centre in the current item matches the current user's centre
        if (currentItem.sf_centre != currentUserCentre) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            return
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val acceptButton = holder.adapterBinding.acceptbutton
            val declineButton = holder.adapterBinding.declinebutton

            holder.adapterBinding.cvStudname.text = currentItem.studentName
            holder.adapterBinding.cvStudemail.text = currentItem.studentEmailId
            holder.adapterBinding.dtCity.text = "Selected City : " + currentItem.sf_city
            holder.adapterBinding.dtCentre.text = "Selected Center : " + currentItem.sf_centre
            holder.adapterBinding.dtSlot.text = "Selected Slot : " + currentItem.sf_slot

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
    private fun updateStatus(item: examdata,
                             status: String,
                             callback: (Boolean) -> Unit) {
        val applicationRef = FirebaseDatabase.getInstance().reference.child(DBConstants.APPLICATION)
        val adminRequestsRef = FirebaseDatabase.getInstance().reference.child(DBConstants.ADMINREQUESTS)

        val query = applicationRef.orderByChild("sf_centre").equalTo(item.sf_centre)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var success = false

                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue(examdata::class.java)

                    if (data != null && data.sf_city == item.sf_city && data.sf_slot == item.sf_slot) {

                        // Updating the status and save it in the ADMINREQUESTS table
                        data.status = status

                        // Set countid to 0 when the status is "Declined"
                        if (status == "Declined") {
                            data.countid = 0
                        }

                        adminRequestsRef.push().setValue(data)
                            .addOnSuccessListener {
                                success = true
                                // Remove the data from the APPLICATION table
                                snapshot.ref.removeValue()
                                    .addOnSuccessListener {
                                        callback.invoke(success)
                                    }
                                    .addOnFailureListener {
                                        callback.invoke(success)
                                        context.showSnackBar("Failed to remove data")
                                    }
                            }
                            .addOnFailureListener {
                                callback.invoke(success)
                                context.showSnackBar("Failed to save data")
                            }
                        break // Exit the loop after updating the status for the matched item
                    }
                }

                if (!dataSnapshot.hasChildren()) {
                    callback.invoke(success)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

}