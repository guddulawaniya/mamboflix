package com.mamboflix.ui.adapter.moviestab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.RowMovieTypeBinding
import java.util.*

class MovieAllMoviesAdapter(
    val context: Context
    /*var listData: ArrayList<CategoryContentModel> = ArrayList()*/) : RecyclerView.Adapter<MovieAllMoviesAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var movieAllListAdapter: MovieAllListMoviesAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var charStringFinal: String = ""



    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        var binding: RowMovieTypeBinding = DataBindingUtil.bind(view)!!

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
                .inflate(R.layout.row_movie_type, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*val model = listData[position]
        holder.binding.tvName.text = model.name

        if (model.isSelected){
            holder.binding.ivName.setImageResource(R.drawable.check)
        }else{
            holder.binding.ivName.setImageResource(R.drawable.uncheck)
        }*/
        setRecyclerview(holder)

        if (position%2==0){
            holder.binding.tvName.text ="Trending Tv Shows"
        }else{
            holder.binding.tvName.text ="Most Watched"
        }
    }



    override fun getItemCount(): Int {
        return 10
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }
    private fun setRecyclerview(mHolder:MyViewHolder) {
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        movieAllListAdapter = MovieAllListMoviesAdapter(context)

        mHolder.binding!!.rvMovieType!!.layoutManager = layoutManager
        mHolder.binding!!.rvMovieType!!.adapter = movieAllListAdapter

        movieAllListAdapter!!.setOnItemClickListener(object : MovieAllListMoviesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                //doIncrease();
/*
                when (view.id) {
                    R.id.cardll ->
                        CommonUtils.setFragment(
                            ProductDeatilsfragment(),
                            false,
                            requireActivity(),
                            R.id.frameContainer
                        )
                }*/

            }


        })
    }
}