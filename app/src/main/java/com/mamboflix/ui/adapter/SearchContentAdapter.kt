package com.mamboflix.ui.adapter

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
import com.mamboflix.databinding.RowHomeListBinding
import com.mamboflix.model.searchContent.SearchContentModel
import java.util.*

class SearchContentAdapter(
    val context: Context,
    var listData: ArrayList<SearchContentModel> = ArrayList()
) : RecyclerView.Adapter<SearchContentAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    private var charStringFinal: String = ""

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var binding: RowHomeListBinding = DataBindingUtil.bind(view)!!

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
                .inflate(R.layout.row_home_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = listData[position]


        Glide.with(context)
            .load(Uri.parse(model.image))
            /*.apply(RequestOptions.fitCenterTransform())*/
            .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
            .apply(RequestOptions.errorOf(R.drawable.image_loading))
            .into(holder.binding.ivUser)

        /*if (position%2==0){
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