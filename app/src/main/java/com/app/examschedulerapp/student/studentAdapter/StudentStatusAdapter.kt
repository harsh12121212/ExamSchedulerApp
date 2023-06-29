package com.app.examschedulerapp.student.studentAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.data.Examdata
import com.app.examschedulerapp.databinding.StudentcardviewBinding

class StudentStatusAdapter(
    private val list: List<Examdata>
) : RecyclerView.Adapter<StudentStatusAdapter.UserViewHolder>() {

    inner class UserViewHolder(val adapterBinding: StudentcardviewBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = StudentcardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = list[position]
        holder.adapterBinding.tvStudname.text = currentItem.studentName
        holder.adapterBinding.tvStudemail.text = currentItem.studentEmailId
        holder.adapterBinding.dtStudCity.text = "Selected City: ${currentItem.sf_city}"
        holder.adapterBinding.dtStudCentre.text = "Selected Center: ${currentItem.sf_centre}"
        holder.adapterBinding.dtStudDate.text = "Selected Date: ${currentItem.sf_examdate}"
        holder.adapterBinding.tvStudStatus.text = currentItem.status
    }

    override fun getItemCount(): Int {
        return list.size
    }
}