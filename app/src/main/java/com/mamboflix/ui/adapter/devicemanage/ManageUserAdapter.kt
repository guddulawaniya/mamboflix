package com.mamboflix.ui.adapter.devicemanage

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.CreateWatchUserModel
import java.util.*

class ManageUserAdapter(
    val context: Context,var listData: ArrayList<CreateWatchUserModel> = ArrayList(), var loginID:String
) : RecyclerView.Adapter<ManageUserAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var my_id:String = ""
    private var my_name:String = ""
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowDeviceListBinding = DataBindingUtil.bind(view)!!

        init {
            view.setOnClickListener(this)
            binding.ivEdit.setOnClickListener(this)
            binding.ivDelete.setOnClickListener(this)
        }


        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(my_id,my_name,adapterPosition, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_device_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = listData[holder.adapterPosition]
        holder.binding.tvTitle.text = model.name
        if (loginID == model.id.toString()){
            holder.binding.tvTitle.setTextColor(Color.parseColor("#F36934"))
        }

        my_id = model.id
        my_name = model.name
        if (loginID==model.id){
            holder.binding.ivDelete.visibility = View.GONE
            holder.binding.tvAccountHolder.visibility = View.GONE
        }else{
            holder.binding.ivDelete.visibility = View.VISIBLE
            holder.binding.tvAccountHolder.visibility = View.GONE
        }



    }


    override fun getItemCount(): Int {
        return listData.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(id:String,name:String,position: Int, view: View)
    }




}