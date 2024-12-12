package com.mamboflix.ui.activity.contentdetails

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.SessionListItemviewBinding
import com.mamboflix.model.contentdetails.SessionList

class SessionListAdapter(
    private val context: Context,
    private var list: List<SessionList>,
    private var click: SesionItemClick
) : RecyclerView.Adapter<SessionListAdapter.Holder>() {

    private var selectedItem = 0
    private var nightMode = true

    class Holder(private val binding: SessionListItemviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(session: SessionList, isSelected: Boolean, nightMode: Boolean, click: SesionItemClick, position: Int) {
            binding.tvSessionName.text = session.seasion
            binding.constraintItemview.setOnClickListener {
                click.click_seasion_item(position, session.seasion_id)
            }

            // Set background based on selection and night mode
            val backgroundRes = if (isSelected) {
                if (nightMode) R.drawable.bg_round_payment else R.drawable.session_red_background
            } else {
                R.drawable.bg_round_payment
            }
            binding.constraintItemview.setBackgroundResource(backgroundRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = SessionListItemviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // Determine if this item is selected
        val isSelected = selectedItem == position

        // Update night mode status
        val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        nightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES

        // Bind data to view
        holder.bind(list[position], isSelected, nightMode, click, position)

        holder.itemView.setOnClickListener {
            val previousItem = selectedItem
            selectedItem = holder.adapterPosition
            notifyItemChanged(previousItem)
            notifyItemChanged(position)
            Log.d("TAG", "onBindViewHolder: ${list[position].seasion_id}")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface SesionItemClick {
        fun click_seasion_item(position: Int, seasion_id: String)
    }
}
