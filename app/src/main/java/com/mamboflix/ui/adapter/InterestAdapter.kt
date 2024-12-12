package com.mamboflix.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.RowInterestListBinding
import com.mamboflix.model.interest.InterestModel
import java.util.*

class InterestAdapter(
    val context: Context,
    var listData: ArrayList<InterestModel> = ArrayList()) : RecyclerView.Adapter<InterestAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    private var charStringFinal: String = ""

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowInterestListBinding = DataBindingUtil.bind(view)!!

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
                .inflate(R.layout.row_interest_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = listData[position]
        holder.binding.tvName.text = model.title

        if (model.isSelected){
            holder.binding.lytTopNew.setBackgroundResource(R.drawable.round_corner_white)
            holder.binding.ivName.setImageResource(R.drawable.check)
            holder.binding.tvName.setTextSize(17f)
        }else{
            holder.binding.lytTopNew.setBackgroundResource(R.drawable.round_corner_grey)
            holder.binding.ivName.setImageResource(R.drawable.uncheck)
        }
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