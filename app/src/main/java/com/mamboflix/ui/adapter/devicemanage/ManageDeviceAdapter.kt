package com.mamboflix.ui.adapter.devicemanage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.managedevice.DeviceModel
import java.util.*

class ManageDeviceAdapter(
    val context: Context,var listData: ArrayList<DeviceModel> = ArrayList()
) : RecyclerView.Adapter<ManageDeviceAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowDeviceListBinding = DataBindingUtil.bind(view)!!

        init {
            view.setOnClickListener(this)
            binding.ivDelete.setOnClickListener(this)
            binding.ivEdit.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_device_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val model = listData[position]
        holder.binding.ivEdit.visibility = View.GONE

        if (holder.adapterPosition==0){
            holder.binding.tvAccountHolder.visibility = View.VISIBLE
            holder.binding.ivDelete.visibility = View.INVISIBLE
        }else{
            holder.binding.tvAccountHolder.visibility = View.GONE
        }

        holder.binding.tvTitle.text = "${model.device_name} ( ${model.name} )"
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