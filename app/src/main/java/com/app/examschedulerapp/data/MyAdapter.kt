package com.app.examschedulerapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.examschedulerapp.R

class MyAdapter(private val examdata: ArrayList<examdata>):RecyclerView.Adapter<MyAdapter.MyviewHolder>() {
    class MyviewHolder(itemView: View)RecyclerView.ViewHolder(itemview){
            val Name:TextView=admincv.findViewById(R.id.name)
             val Email:TextView=itemView.findViewById(R.id.email)
            val Center:TextView=itemView.findViewById(R.id.center)
             val Class:TextView=itemView.findViewById(R.id.class)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyviewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        holder.tvname.text=userList[position].name
        holder.tvname.text=userList[position].name,
        holder.tvname.text=userList[position].name,
        holder.tvname.text=userList[position].name,
        holder.tvname.text=userList[position].name
    }

    override fun getItemCount(): Int {
        return Userlist.size
    }
}