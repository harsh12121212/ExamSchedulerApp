package com.app.examschedulerapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.StudentStatusFragment
import com.app.examschedulerapp.databinding.StudentcardviewBinding

class StudentStatusAdapter (
    var context: StudentStatusFragment,
    var list: ArrayList<examdata>): RecyclerView.Adapter<StudentStatusAdapter.UserViewHolder>(){

    inner class UserViewHolder(val adapterBinding : StudentcardviewBinding)
            : RecyclerView.ViewHolder(adapterBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentStatusAdapter.UserViewHolder {
        val  binding = StudentcardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.adapterBinding.tvStudname.text=list[position].studentName
        holder.adapterBinding.dtStudCity.text=list[position].sf_city
        holder.adapterBinding.dtStudCentre.text=list[position].sf_centre
        holder.adapterBinding.dtStudDate.text=list[position].sf_examdate
        holder.adapterBinding.tvStudemail.text=list[position].studentEmailId
    }

    override fun getItemCount(): Int {
        return list.size
    }

}