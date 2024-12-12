package com.mamboflix.ui.adapter.subuser

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.CreateWatchUserModel
import java.util.*

class SubUserAdapter(
    val context: Context,var listData: ArrayList<CreateWatchUserModel> = ArrayList()
) : RecyclerView.Adapter<SubUserAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowSubuserBinding = DataBindingUtil.bind(view)!!

        init {
            view.setOnClickListener(this)
        }


        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v,listData[position].id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_subuser, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = listData[holder.adapterPosition]
        if (!model.image.isNullOrBlank()) {
            Glide.with(context).load(Uri.parse(model.image))
                    .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
                    .apply(RequestOptions.errorOf(R.drawable.user_profile))
                    .into(holder.binding.ivUser)
        }
        holder.binding.tvName.text = model.name
    }


    override fun getItemCount(): Int {
        return listData.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View,id:String)
    }




}