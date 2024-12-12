package com.mamboflix.ui.adapter.mylist

import android.content.Context
import android.net.Uri
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
import com.mamboflix.databinding.RowMyListBinding
import com.mamboflix.model.mylist.MyListModel
import java.util.*

class MyListAdapter(
    val context: Context,
    var listData: ArrayList<MyListModel> = ArrayList()) : RecyclerView.Adapter<MyListAdapter.MyViewHolder>(), Filterable {
    private var listener: OnItemClickListener? = null
    var mFilterList:ArrayList<MyListModel>?=listData
    private var charStringFinal: String = ""

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener,View.OnLongClickListener{

        var binding: RowMyListBinding = DataBindingUtil.bind(view)!!

        init {

            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClick(adapterPosition, v, mFilterList!! )
        }

        override fun onLongClick(v: View?): Boolean {
            if (listener != null)
                listener!!.onLongPress(adapterPosition, v!!, mFilterList!!)
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_my_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var model = mFilterList!![holder.adapterPosition]
        Glide.with(context)
                .load(Uri.parse(model!!.single_content.image))
        .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                .apply(RequestOptions.errorOf(R.drawable.image_loading))
                .into(holder.binding!!.ivUser)
    }
    override fun getItemCount(): Int {
        return mFilterList!!.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View, filterList: ArrayList<MyListModel>)
        fun onLongPress(position: Int,view: View,filterList: ArrayList<MyListModel>)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mFilterList = if (charString.isEmpty()) {
                    listData
                } else {
                    val filteredList: ArrayList<MyListModel> = ArrayList()
                    for (model in listData) {
                        if (model.single_content.title.toLowerCase(Locale.getDefault()).contains(charString)) {
                            filteredList.add(model)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mFilterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilterList = filterResults.values as ArrayList<MyListModel>?
                notifyDataSetChanged()
            }
        }
    }




}