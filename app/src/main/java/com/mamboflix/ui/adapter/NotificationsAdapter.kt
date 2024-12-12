package com.mamboflix.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.NotificationModel
import java.util.*

class NotificationsAdapter(
    val context: Context,
    var listData: ArrayList<NotificationModel> = ArrayList()) : RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    private var charStringFinal: String = ""

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowNotificationsListBinding = DataBindingUtil.bind(view)!!

        init {

            view.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_notifications_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
          holder.binding.tvName.text= listData[holder.adapterPosition].title_e
          holder.binding.description.text= listData[holder.adapterPosition].message_e
          holder.binding.tvCreatedDate.text= "Created at:- "+listData[holder.adapterPosition].created_at


       /* if (position%2==0){
            holder.binding.ivUser.setImageResource(R.drawable.b1)
        }else{
            holder.binding.ivUser.setImageResource(R.drawable.b2)
        }*/
    }



    override fun getItemCount(): Int {
        return listData.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }




}