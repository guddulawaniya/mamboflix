package com.mamboflix.ui.adapter.moviestab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.RowPreviewsListBinding
import java.util.*

class PreviewsMoviesAdapter(
    val context: Context
   /* var listData: ArrayList<PreviewModel> = ArrayList()*/) : RecyclerView.Adapter<PreviewsMoviesAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    private var charStringFinal: String = ""

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowPreviewsListBinding = DataBindingUtil.bind(view)!!

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
                .inflate(R.layout.row_previews_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*val model = listData[position]
        holder.binding.tvName.text = model.name

        if (model.isSelected){
            holder.binding.ivName.setImageResource(R.drawable.check)
        }else{
            holder.binding.ivName.setImageResource(R.drawable.uncheck)
        }*/

        if (position%2==0){
            holder.binding.ivUser.setImageResource(R.drawable.p1)
        }else{
            holder.binding.ivUser.setImageResource(R.drawable.p2)
        }
    }



    override fun getItemCount(): Int {
        return 10
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }

}