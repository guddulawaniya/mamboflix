package com.mamboflix.ui.adapter.dashboard.DashboardMovieTab

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.R
import com.mamboflix.databinding.RowMovieTypeBinding
import com.mamboflix.model.hometab.CategoryModel
import com.mamboflix.model.movieTabModel.CategoryContentMovieTabModel
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.adapter.dashboard.MovieAllListAdapter
import java.util.ArrayList

class MovieAllAdapterMovieTab(
    val context: Context,
    var listData: ArrayList<CategoryContentMovieTabModel> = ArrayList(), var type:String) : RecyclerView.Adapter<MovieAllAdapterMovieTab.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var movieAllListAdapter: MovieAllListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var charStringFinal: String = ""
    var listCategoryData: ArrayList<CategoryModel>?= null

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
        val model = listData[position]

        listCategoryData = ArrayList()
        listCategoryData!!.clear()
        listCategoryData!!.addAll(model!!.content)


        if (model!!.content!=null && model!!.content.size>0){
            holder.binding.tvName.visibility = View.VISIBLE
        }else{
            holder.binding.tvName.visibility = View.GONE
        }
        holder.binding.tvName.text = model.title
        setRecyclerview(holder,listCategoryData!!)

    }



    override fun getItemCount(): Int {
        return listData.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }
    private fun setRecyclerview(mHolder:MyViewHolder, myModel: ArrayList<CategoryModel>) {
        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        movieAllListAdapter = MovieAllListAdapter(context,myModel)

        mHolder.binding!!.rvMovieType!!.layoutManager = layoutManager
        mHolder.binding!!.rvMovieType!!.adapter = movieAllListAdapter

        movieAllListAdapter!!.setOnItemClickListener(object : MovieAllListAdapter.OnItemClickListener {
            override fun onItemClick(position:
                                     Int, view: View
            ) {
                if (type=="movie"){
                    context.startActivity(
                        Intent(context, ContentDetailsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("content_id",myModel!![position].id)
                        .putExtra("watch_time","0")
                        .putExtra("episode_id","")
                        .putExtra("fragment_type","movie"))

                }else{
                    context.startActivity(
                        Intent(context, ContentDetailsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("content_id",myModel!![position].id)
                        .putExtra("watch_time","0")
                        .putExtra("episode_id",""))
                }
                //doIncrease();
            }
        })
    }
}