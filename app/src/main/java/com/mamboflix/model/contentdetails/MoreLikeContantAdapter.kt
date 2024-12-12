package com.mamboflix.model.contentdetails

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
import java.util.ArrayList

class MoreLikeContantAdapter (
    val context: Context,
    var listData: ArrayList<MoreLikeThisData> = ArrayList()
) : RecyclerView.Adapter<MoreLikeContantAdapter.MyViewHolder>() {
    private var listener: OnItemClickListenerMoreLike? = null

    private var charStringFinal: String = ""

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowHomeListBinding = DataBindingUtil.bind(view)!!

        init {

            view.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            if (listener != null)
                listener!!.onItemClickMoreLike(adapterPosition, v)
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
            .load(Uri.parse(model!!.image_e))
            /*.apply(RequestOptions.fitCenterTransform())*/
            .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
            .apply(RequestOptions.errorOf(R.drawable.image_loading))
            .into(holder.binding.ivUser)
    }



    override fun getItemCount(): Int {
        return listData.size
    }

    fun setOnItemClickListener(listener: OnItemClickListenerMoreLike) {
        this.listener = listener
    }

    interface OnItemClickListenerMoreLike {
        fun onItemClickMoreLike(position: Int, view: View)
    }

}