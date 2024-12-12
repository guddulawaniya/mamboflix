package com.mamboflix.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mamboflix.R
import com.mamboflix.databinding.MoreLikeListItemviewBinding
import com.mamboflix.model.contentdetails.MoreLikeThisData
import com.mamboflix.prefs.UserPref

class MoreLikeThisAdapter(
    private val context: Context,
    private var list: List<MoreLikeThisData>,
    private val click: MoreLikeThisClick
) : RecyclerView.Adapter<MoreLikeThisAdapter.Holder>() {
    private lateinit var userPref1: UserPref

    inner class Holder(private val binding: MoreLikeListItemviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoreLikeThisData) {
            val imageUrl = if (userPref1.getPreferredLanguage() == "English") {
                item.image_e
            } else {
                item.image
            }

            Glide.with(context)
                .load(Uri.parse(imageUrl))
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                .apply(RequestOptions.errorOf(R.drawable.image_loading))
                .into(binding.ivUser)

            binding.ivUser.setOnClickListener {
                click.MoreLikeThisItemClick(
                    adapterPosition,
                    item.path,
                    item.id.toString(),
                    item.content_duration,
                    item.content_type.toString(),
                    item.content_mode,
                    item.is_subscribed,


                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = MoreLikeListItemviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        userPref1 = UserPref(context)
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MoreLikeThisClick {
        fun MoreLikeThisItemClick(
            position: Int,
            playUrl: String,
            contentId: String,
            watch_duration: String,
            type: String,
            content_mode: Int,
            is_subscribed: Int,
        )
    }
}
