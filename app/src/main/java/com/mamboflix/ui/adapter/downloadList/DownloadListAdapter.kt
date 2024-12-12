package com.mamboflix.ui.adapter.downloadList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mamboflix.R
import com.mamboflix.databinding.*
import java.io.File
import java.util.*

class DownloadListAdapter(
    val context: Context,
    var listData: ArrayList<File>) : RecyclerView.Adapter<DownloadListAdapter.MyViewHolder>(),Filterable {
    private var listener: OnItemClickListener? = null
    private var mFilterList:ArrayList<File>?=listData
    private var charStringFinal: String = ""

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener, View.OnLongClickListener {

        var binding: RowDownloadListBinding = DataBindingUtil.bind(view)!!

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v)
        }

        override fun onLongClick(v: View?): Boolean {
            if (listener != null)
                listener!!.onLongPress(adapterPosition, v!!)
                return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_download_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var model = mFilterList!![holder.adapterPosition]
//        Glide.with(context)
//                .load("file:///"+model.absolutePath)
//                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
//                .apply(RequestOptions.errorOf(R.drawable.image_loading))
//                .into(holder.binding.ivUser)

        Glide.with(context)
            .load("file:///"+model.absolutePath)
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
        return mFilterList!!.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
        fun onLongPress(position: Int,view: View)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mFilterList = listData
                } else {
                    val filteredList: ArrayList<File> = ArrayList()
                    for (model in listData) {
                        if (model.name.toLowerCase(Locale.getDefault()).contains(charString)) {
                            filteredList.add(model)
                        }
                    }
                    mFilterList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mFilterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilterList = filterResults.values as ArrayList<File>?
                notifyDataSetChanged()
            }
        }
    }


}