package com.mamboflix.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.SubtitleModel
import java.util.*

class SubtitlesListAdapter(
    val context: Context,
    var listData: ArrayList<SubtitleModel>, var rowIndex:Int) : RecyclerView.Adapter<SubtitlesListAdapter.MyViewHolder>(){
    private var listener: OnItemClickListener? = null


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: LayoutRowItemSubtitlesBinding = DataBindingUtil.bind(view)!!

        init {

            binding.cbSubtitlesItemPopup.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            if (listener != null)
              //  rowIndex=adapterPosition
                listener!!.onItemClick(adapterPosition, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_row_item_subtitles, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(rowIndex==position){

            holder.binding.cbSubtitlesItemPopup.isChecked=true
        }
        else{
            holder.binding.cbSubtitlesItemPopup.isChecked=false
        }
        var model = listData[holder.adapterPosition]
        holder.binding.cbSubtitlesItemPopup.text=model.language


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