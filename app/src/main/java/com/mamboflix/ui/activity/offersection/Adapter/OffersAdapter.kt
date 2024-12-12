package com.vaidic.guru.ui.activity.register.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.LayoutOffersBinding
import com.mamboflix.ui.activity.offersection.offers.OffersList


 class OffersAdapter(
    private val context: Context,
    val list: ArrayList<OffersList>
) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        var binding: LayoutOffersBinding = DataBindingUtil.bind(itemView)!!

        init {

           binding!!.ll.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v)
        }



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.layout_offers, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model: OffersList = list.get(holder.adapterPosition)
        if (model != null) {
            holder.binding!!.off.text = if (model.off_percentage != null) model.off_percentage+"% OFF" else ""
            holder.binding!!.flashsale.text = if (model.coupon_code != null) model.coupon_code else ""
            holder.binding!!.description.text = if (model.description != null) model.description else ""

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }





}