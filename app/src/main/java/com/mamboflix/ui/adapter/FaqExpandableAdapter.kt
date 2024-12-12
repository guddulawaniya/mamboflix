package com.mamboflix.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.model.FaqMainModel

class FaqExpandableAdapter(
    private val context: Context,
    private val faqList: ArrayList<FaqMainModel>?
) : RecyclerView.Adapter<FaqExpandableAdapter.ViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()

    // Return the size of the faqList
    override fun getItemCount(): Int {
        return faqList?.size ?: 0
    }

    // Create and return a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.faq_header, parent, false)
        return ViewHolder(itemView)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faqItem = faqList?.get(position)

        // Bind the data to the views in the ViewHolder
        holder.headertext.text = faqItem?.title
        holder.question.text = faqItem?.description

        if (expandedPositions.contains(position)) {
            holder.question.visibility = View.VISIBLE
        } else {
            holder.question.visibility = View.GONE
        }

        // Handle item click to toggle expanded state
        holder.itemView.setOnClickListener {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position) // Collapse item
            } else {
                expandedPositions.add(position) // Expand item
            }
            // Notify item change to update the visibility state
            notifyItemChanged(position)
        }


    }

    // ViewHolder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headertext = itemView.findViewById<TextView>(R.id.tvTitle)
        val question = itemView.findViewById<TextView>(R.id.question)
    }



  /*  inner class NestedFaqAdapter(
        private val nestedFaqList: List<String> // Assuming description is a list of strings (could be other types)
    ) : RecyclerView.Adapter<NestedFaqAdapter.NestedViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.faq_child, parent, false)
            return NestedViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: NestedViewHolder, position: Int) {
            val descriptionItem = nestedFaqList[position]
            holder.descriptionText.text = descriptionItem
        }

        override fun getItemCount(): Int {
            return nestedFaqList.size
        }

        // ViewHolder class for the nested RecyclerView
        inner class NestedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val descriptionText: TextView = itemView.findViewById(R.id.tvMessage)
        }
    }*/
}




