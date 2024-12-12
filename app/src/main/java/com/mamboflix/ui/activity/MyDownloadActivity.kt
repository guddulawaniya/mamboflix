package com.mamboflix.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityMylistBinding
import com.mamboflix.databinding.DialogCustomBinding
import com.mamboflix.ui.activity.player.PlayerViewActivity
import com.mamboflix.ui.adapter.downloadList.DownloadListAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyDownloadActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMylistBinding
    private var myListAdapter: DownloadListAdapter? = null
    private var fileList:ArrayList<File>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_mylist)
        fileList= ArrayList()
        /*binding!!.nsvMain.visibility = View.VISIBLE
        binding!!.shimmerFrameLayout.stopShimmerAnimation()*/
        binding.shimmerFrameLayout.visibility = View.GONE
        setToolBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    private fun setToolBar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = " "
        binding.toolbar.navigationIcon?.setColorFilter(
                this.resources.getColor(R.color.white),
                PorterDuff.Mode.SRC_ATOP
        )
        binding.toolbar.title= resources.getString(R.string.myDownloads)
        binding.tvTitle.text=resources.getString(R.string.myDownloads)
        setRecyclerview()
//callGetContentApi()
        getDownloadedVideos()
       }

    private fun setRecyclerview() {
        myListAdapter = DownloadListAdapter(this, fileList!!)
        binding.rvMyList.adapter = myListAdapter

        myListAdapter!!.setOnItemClickListener(object : DownloadListAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View) {
                val url = fileList!![position]
                val bundle = Bundle()
                bundle.putString("playUrl", "file:///$url")
                bundle.putString("watch_duration","0")
                bundle.putBoolean("downloadkey",true)
                bundle.putString("title",fileList!![position].name)
                val intent = Intent(this@MyDownloadActivity, PlayerViewActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onLongPress(position: Int, view: View) {

                confirmationAlert(fileList!![position])

            }
        })
    }

    override fun onClick(view: View?) {


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val alertMenuItem = menu!!.findItem(R.id.action_search)
        val searchView = alertMenuItem!!.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myListAdapter!!.filter.filter((newText.toString()))
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDownloadedVideos() {

        val path = applicationContext.filesDir.absolutePath + "/Movies/${userPref.getSubUserId().toString()}" // Folder per user
        val directory = File(path)

        // Check if the user's folder exists
        if (directory.exists()) {
            fileList!!.clear() // Clear previous list before adding new ones
            fileList!!.addAll(directory.listFiles().toList()) // Add all files in the user's folder

            myListAdapter!!.notifyDataSetChanged() // Notify the adapter about data change
            binding.tvNoData.visibility = View.GONE

            val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date()) // Get current date

            // Iterate through the files and check expiration date
            for (i in 0 until fileList!!.size) {
                val lastModified = Date(fileList!!.get(i).lastModified())
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val formattedDateString = formatter.format(lastModified)

                Log.v("date", formattedDateString) // Log the formatted date

                // Calculate expiration date (30 days after the last modified date)
                val c1 = Calendar.getInstance()
                c1.time = lastModified
                c1.add(Calendar.DAY_OF_YEAR, 30)
                val expirationDate = formatter.format(c1.time)
                Log.v("date", expirationDate)

                // Compare expiration date with the current date
                if (currentDate == expirationDate) {
                    if (i == 0) {
                        utils.simpleAlert(this, "Alert", "Your downloaded video is expired.")
                    }

                    // Delete the expired video file
                    if (fileList!!.get(i).exists()) {
                        fileList!!.get(i).delete()
                    }

                    // Remove the expired video from the list
                    fileList!!.removeAt(i)

                    // Notify the adapter that data has changed
                    myListAdapter!!.notifyDataSetChanged()

                    // Check if the list is empty and show a message
                    if (fileList!!.isEmpty()) {
                        binding.tvEmptyDownload.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            // If the user's folder doesn't exist, show "No downloads yet"
            binding.tvEmptyDownload.text = "No downloads yet"
            binding.tvEmptyDownload.visibility = View.VISIBLE
        }
    }

    /*private fun getDownloadedVideos(){
        val path: String =applicationContext.filesDir.absolutePath.toString()+"/Movies"
        val directory = File(path)
        if(directory.exists()) {

            fileList!!.addAll(directory.listFiles().toList())

            myListAdapter!!.notifyDataSetChanged()
            binding.tvNoData.visibility = View.GONE

            for (i in 0 until fileList!!.size) {
                val lastModified = Date(fileList!!.get(i).lastModified())
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val formattedDateString: String = formatter.format(lastModified)
                Log.v("date",formattedDateString)
                val date = Date()
                var df = SimpleDateFormat("yyyy-MM-dd")
                val c1 = Calendar.getInstance()
                val currentDate = df.format(date) // get current date here
                Log.v("date",currentDate)
                // now add 30 day in Calendar instance
                // now add 30 day in Calendar instance
                c1.setTime(lastModified);
                c1.add(Calendar.DAY_OF_YEAR, 30)
                df = SimpleDateFormat("yyyy-MM-dd")
                val resultDate = c1.time
                val dueDate = df.format(resultDate)
                Log.v("date",dueDate)
                if(currentDate.equals(dueDate.toString())){
                    if(i==0){
                        utils.simpleAlert(this,"Alert","Your downloaded video is expired.")
                    }
                    if(fileList!!.get(i).exists()){
                        fileList!!.get(i).delete()
                    }
                    fileList!!.remove(fileList!!.get(i))
                    myListAdapter!!.notifyDataSetChanged()
                    if(fileList!!.isEmpty()){
                        binding.tvEmptyDownload.visibility=View.VISIBLE
                    }
                }
            }
        }
        if(fileList!!.isEmpty()){
            binding.tvEmptyDownload.text = "No downloads yet"
            binding.tvEmptyDownload.visibility=View.VISIBLE
        }
        }*/



    private fun confirmationAlert(file: File) {
        var cDialog = Dialog(this,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogCustomBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_custom,
            null,
            false
        )
        cDialog.setContentView(bindingDialog.root)
        cDialog.setCancelable(false)
        cDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}


        cDialog.show()
        bindingDialog.tvTitle.text = getString(R.string.confirmation)
        bindingDialog.tvMsg.text = getString(R.string.do_you_want)

        bindingDialog.btOk.setOnClickListener {
            cDialog.dismiss()
            if(file.exists()){
                file.delete()
            }
            fileList!!.remove(file)
            myListAdapter!!.notifyDataSetChanged()
            if(fileList!!.isEmpty()){
                binding.tvEmptyDownload.visibility=View.VISIBLE
            }

        }

        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }



        /* val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
         builder.setTitle("Confirmation")
         builder.setMessage("Do you want to remove this movie?")
         builder.setPositiveButton("Yes") { dialogInterface, i ->

             callMakeListApi("0",file)


         }
         builder.setNegativeButton("No", null)
         builder.create()
         builder.show()*/
    }

   /* private fun confirmationAlert(file: File) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle(getString(R.string.confirmation))
        builder.setMessage("Do you want to remove this movie?")
        builder.setPositiveButton("Yes") { dialogInterface, i ->
            if(file.exists()){
                file.delete()
            }
            fileList!!.remove(file)
            myListAdapter!!.notifyDataSetChanged()
            if(fileList!!.isEmpty()){
                binding.tvEmptyDownload.visibility=View.VISIBLE
            }

        }
        builder.setNegativeButton("No", null)
        builder.create()
        builder.show()
    }*/

}