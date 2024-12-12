package com.vaidic.guru.ui.activity.register.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.LayoutHistoryBinding
import com.mamboflix.ui.activity.purchsedhistory.paymenthistory.history


class purchasedAdapter(private val context: Context, val list: ArrayList<history>,
                       private var unsub :unsubscribed, var from:String) : RecyclerView.Adapter<purchasedAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: LayoutHistoryBinding = DataBindingUtil.bind(itemView)!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.layout_history, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: history = list[holder.adapterPosition]
        holder.binding.name.text = model.package_name
        holder.binding.price.text = model.amount
        holder.binding.expirydate.text = model.expiry_date

        unsub.dataforunsubscribed(model.id.toString(),holder.binding.purchsed)
        if (from == "FromHome"){

            if (model.status == 1) holder.binding.purchsed.visibility = View.VISIBLE  else holder.binding.purchsed.visibility = View.GONE

        }else{
            holder.binding.purchsed.visibility = View.GONE
        }

        holder.binding.status.text =model.payment_status


    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface unsubscribed{
         fun dataforunsubscribed(id:String,layout:TextView)
    }

}