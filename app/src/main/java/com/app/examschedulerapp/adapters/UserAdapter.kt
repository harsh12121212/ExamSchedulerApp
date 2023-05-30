package com.app.examschedulerapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.AdminDashboardFragment
import com.app.examschedulerapp.R
import com.app.examschedulerapp.data.DBConstants
import com.app.examschedulerapp.data.examdata
import com.app.examschedulerapp.databinding.AdmincardviewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserAdapter(
    var context: AdminDashboardFragment,
    var list: ArrayList<examdata>): RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    inner class UserViewHolder(val adapterBinding :AdmincardviewBinding)
        :RecyclerView.ViewHolder(adapterBinding.root){}

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
      val  binding = AdmincardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = list[position]

        val acceptButton = holder.adapterBinding.acceptbutton
        val declineButton = holder.adapterBinding.declinebutton
        val statusText = holder.adapterBinding.cvStudexamstatus

        holder.adapterBinding.cvStudname.text=currentItem.studentName
        holder.adapterBinding.dtCity.text=currentItem.sf_city
        holder.adapterBinding.dtCentre.text=currentItem.sf_centre
        holder.adapterBinding.cvStudemail.text=currentItem.studentEmailId

        acceptButton.visibility = View.VISIBLE
        declineButton.visibility = View.VISIBLE
        statusText.visibility = View.INVISIBLE

        // Set initial visibility based on the status
        if (currentItem.status == "Accepted" || currentItem.status == "Declined") {
            acceptButton.visibility = View.INVISIBLE
            declineButton.visibility = View.INVISIBLE
            statusText.text = currentItem.status
            statusText.visibility = View.VISIBLE
        }

        // Set click listener for the accept button
        acceptButton.setOnClickListener {
            updateStatus(currentItem, "Accepted") { success ->
                if (success) {
                    acceptButton.visibility = View.INVISIBLE
                    declineButton.visibility = View.INVISIBLE
                    statusText.text = "Accepted"
                    statusText.visibility = View.VISIBLE
                } else {
                    context.showSnackBar("Failed to update status")
                }
            }
        }

        // Set click listener for the decline button
        declineButton.setOnClickListener {
            updateStatus(currentItem, "Declined") { success ->
                if (success) {
                    acceptButton.visibility = View.INVISIBLE
                    declineButton.visibility = View.INVISIBLE
                    statusText.text = "Declined"
                    statusText.visibility = View.VISIBLE
                } else {
                    context.showSnackBar("Failed to update status")
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

                    if (status == "Declined") {
                        // Delete the data from the table
                        database.child(key!!).removeValue()
                            .addOnSuccessListener {
                                success = true
                                callback.invoke(success)
                            }
                            .addOnFailureListener {
                                callback.invoke(success)
                                context.showSnackBar("Failed to delete data")
                            }
                    } else {
                        val updateData = HashMap<String, Any>()
                        updateData["status"] = status


                        database.child(key!!).updateChildren(updateData)
                            .addOnSuccessListener {
                                success = true
                                callback.invoke(success)
                            }
                            .addOnFailureListener {
                                callback.invoke(success)
                                context.showSnackBar("Failed to update status")
                            }
                    }
                }
                if (!dataSnapshot.hasChildren()) {
                    callback.invoke(success)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancellation if needed
            }
        })
    }

    override fun getItemCount(): Int {
         return list.size
    }

}