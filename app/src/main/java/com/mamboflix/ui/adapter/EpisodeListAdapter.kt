package com.mamboflix.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mamboflix.R
import com.mamboflix.databinding.MoreLikeListItemviewBinding
import com.mamboflix.model.contentdetails.EpisodeData
import com.mamboflix.prefs.UserPref

class EpisodeListAdapter(
    private val context: Context,
    private var list: List<EpisodeData>,
    private var click: EpisodeClick
) : RecyclerView.Adapter<EpisodeListAdapter.Holder>() {

    private lateinit var userPref1: UserPref

    inner class Holder(private val binding: MoreLikeListItemviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: EpisodeData) {
            val imageUrl = if (userPref1.getPreferredLanguage() == "English") {
                episode.image_e
            } else {
                episode.image_s
            }

            Glide.with(context)
                .load(Uri.parse(imageUrl))
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                .apply(RequestOptions.errorOf(R.drawable.image_loading))
                .into(binding.ivUser)

            binding.ivUser.setOnClickListener {
                click.EpisodeItemClick(
                    adapterPosition,
                    episode.path,
                    episode.id.toString(),
                    episode.content_duration,
                    episode.content_type.toString(),
                    episode.content_mode
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

    interface EpisodeClick {
        fun EpisodeItemClick(
            position: Int,
            playUrl: String,
            contentId: String,
            watch_duration: String,
            type: String,
            content_mode: Int
        )
    }
}
