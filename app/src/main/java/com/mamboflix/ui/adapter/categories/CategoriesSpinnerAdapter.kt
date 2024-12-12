package com.mamboflix.ui.adapter.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.RowCategorySpinnerItemBinding
import com.mamboflix.model.MoviesAndShowsCategory

class CategoriesSpinnerAdapter(val context:Context, private val list:ArrayList<MoviesAndShowsCategory>): RecyclerView.Adapter<CategoriesSpinnerAdapter.ViewHolder>(){
     var binding:RowCategorySpinnerItemBinding?=null
    private var listener:OnItemClickListener?=null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{

        init {
            binding=DataBindingUtil.bind(itemView)
            binding!!.root.setOnClickListener(this)
        }

       override fun onClick(v: View?) {
           listener!!.onItemClick(adapterPosition,v!!)
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_category_spinner_item, parent, false)
   return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val model=list[position]

        binding!!.tvCategory.text=model.title
    }

    override fun getItemCount(): Int {
       return  list.size
    }


}