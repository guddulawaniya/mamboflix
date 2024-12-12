package com.mamboflix.ui.activity.reviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.LayoutReviewsBinding
import com.mamboflix.ui.activity.reviews.reviewsmodel.rating_list


public class ReviewAdapter(private val context: Context, val list: ArrayList<rating_list>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        var binding: LayoutReviewsBinding = DataBindingUtil.bind(itemView)!!

        init {

         //   binding!!.ll.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            when (p0!!.id) {
                /*R.id.ll -> {

                }*/
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.layout_reviews, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model: rating_list = list.get(holder.adapterPosition)
        if (model != null) {
            holder.binding.username.text= model.username
            holder.binding.ratingno.text= ""+model.rating
            holder.binding.description.text= model.description
            holder.binding.myRatingBar.rating=model.rating.toFloat()
            holder.binding.date.text=getSplitedDate(model.created_at)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun getSplitedDate(date: String?): String? {
        return if (date != null) {
            val split = date.split("T".toRegex()).toTypedArray()
            changeDateFormat(split[0])
        } else {
            "No date"
        }
    }

    fun changeDateFormat(date: String): String? {
        val splitDate = date.split("-".toRegex()).toTypedArray()
        return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0]
    }



}