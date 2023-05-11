package com.app.examschedulerapp.data

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.AdminDashboardFragment
import com.app.examschedulerapp.databinding.AdmincardviewBinding

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
        holder.adapterBinding.cvStudname.text=list[position].studentName
        holder.adapterBinding.dtCity.text=list[position].sf_city
        holder.adapterBinding.dtCentre.text=list[position].sf_centre
        holder.adapterBinding.cvStudemail.text=list[position].studentEmailId
    }

    override fun getItemCount(): Int {
         return list.size
    }

}