package com.mamboflix.ui.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseFragment
import com.mamboflix.R
import com.mamboflix.databinding.DialogCustomBinding
import com.mamboflix.databinding.FragmentMylistBinding
import com.mamboflix.model.mylist.MyListModel
import com.mamboflix.ui.activity.MyListActivity
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import com.mamboflix.ui.adapter.mylist.MyListAdapter
import com.mamboflix.utils.CommonUtils
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.Locale


@Suppress("UNREACHABLE_CODE")
class MyListFragment : BaseFragment(), View.OnClickListener {
    private var binding: FragmentMylistBinding? =null
    private var myListAdapter: MyListAdapter? = null
    var myList : ArrayList<MyListModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myList = ArrayList()
        callMyListAPI()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mylist, container, false)
        setHasOptionsMenu(true)
        getCountryName(requireContext())
        binding!!.nsvMain.visibility = View.GONE
        binding!!.shimmerFrameLayout.visibility = View.VISIBLE
        binding!!.shimmerFrameLayout.startShimmer()
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        return binding!!.root
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as HomeActivity).changeIcon(false)
        //(requireActivity() as HomeActivity).changeIcon(true)
        //(requireActivity() as HomeActivity).fragmenttitle("Home", true)

        setRecyclerview()
    }

    private fun setRecyclerview() {
        binding!!.ivSearchContent.setOnClickListener(this)
        binding!!.btAddList.setOnClickListener(this)
        myListAdapter = MyListAdapter(requireContext(),myList!!)
        val linearLayoutManager =
            GridLayoutManager(requireContext(), 4, LinearLayoutManager.VERTICAL, false)
        binding!!.rvMyList.layoutManager = linearLayoutManager
        binding!!.rvMyList.adapter = myListAdapter

        myListAdapter!!.setOnItemClickListener(object : MyListAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View, filterList: ArrayList<MyListModel>) {

                startActivity(Intent(requireContext(), ContentDetailsActivity::class.java)
                    .putExtra("content_id",myList!![position].content_id)
                    .putExtra("episode_id",myList!![position].episode_id)
                    .putExtra("watch_time","0"))

            }
            override fun onLongPress(position: Int, view: View, filterList: ArrayList<MyListModel>) {
                confirmationAlert(filterList!![position])

            }
        })
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSearchContent->{
                val intent=Intent(requireContext(), MyListActivity::class.java)
                startActivity(intent)
            }

            R.id.btAddList->{
                var mfragment = DashboardFragment()
                CommonUtils.setFragment(mfragment, false, requireActivity(), R.id.frameContainer)
            }
        }
    }
    
    private fun callMyListAPI() {
        myList!!.clear()
        apiService.callMyListAPI("Bearer "+userPref.getToken(),userPref.getSubUserId().toString(),
            userPref.getFcmToken().toString(),
        userPref.getPreferredLanguage(),getCountryCode(getCountryName(requireContext()))!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            /*.doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)*/

            .subscribe({ commonResponse ->

                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    //binding!!.swipeContainer.isRefreshing = false
                    binding!!.nsvMain.visibility = View.VISIBLE
                    binding!!.shimmerFrameLayout.stopShimmer()
                    binding!!.shimmerFrameLayout.visibility = View.GONE

                    myList!!.addAll(commonResponse.mdata)

                    myListAdapter!!.notifyDataSetChanged()
                    binding!!.tvNoData.visibility = View.GONE
                    binding!!.llEmptyMyList.visibility=View.GONE

                } else {
                    binding!!.nsvMain.visibility = View.VISIBLE
                    binding!!.shimmerFrameLayout.stopShimmer()
                    binding!!.shimmerFrameLayout.visibility = View.GONE
                    binding!!.llEmptyMyList.visibility=View.VISIBLE
                    /*binding!!.tvNoData.visibility = View.VISIBLE
                    binding!!.tvNoData.text = "No List Data"*/
                    /*utils.simpleAlert(
                        requireContext(),
                        "",
                        "No Data"
                    )*/
                   // hideProgressDialog()
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        getString(R.string.check_network_connection)
                    )

                } else {
                    utils.simpleAlert(
                        requireContext(),
                        "Error",
                        throwable.message.toString())

                }
            })
    }


    private fun callMakeListApi(isStatus:String , mList: MyListModel) {
        apiService.callMakeListApi("Bearer "+userPref.getToken(),userPref.getSubUserId().toString(),mList.content_id!!,mList!!.cat_id,isStatus,userPref.getFcmToken().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               /* .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)*/
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0) {
                        binding!!.nsvMain.visibility = View.VISIBLE
                        binding!!.shimmerFrameLayout.stopShimmer()
                        binding!!.shimmerFrameLayout.visibility = View.GONE
                        myList!!.remove(mList)
                        myListAdapter!!.mFilterList!!.remove(mList)
                        myListAdapter!!.notifyDataSetChanged()
                        binding!!.tvNoData.visibility = View.GONE
                        //binding!!.ivMyList.setColorFilter(ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                        Toast.makeText(requireContext(),""+commonResponse.message, Toast.LENGTH_SHORT).show()

                        if (myList!!.isEmpty()){
                            binding!!.llEmptyMyList.visibility=View.VISIBLE
                        }
                    } else {
                        binding!!.llEmptyMyList.visibility=View.VISIBLE
                        binding!!.nsvMain.visibility = View.VISIBLE
                        binding!!.shimmerFrameLayout.stopShimmer()
                        binding!!.shimmerFrameLayout.visibility = View.GONE
                        //hideProgressDialog()
                    }
                }, { throwable ->
                    //hideProgressDialog()
                    if (throwable is ConnectException) {
                        utils.simpleAlert(
                                requireContext(),
                                "Error",
                                getString(R.string.check_network_connection)
                        )

                    } else {
                        utils.simpleAlert(
                                requireContext(),
                                "Error",
                                throwable.message.toString())
                    }
                })
    }


    private fun confirmationAlert(file: MyListModel) {
        var cDialog = Dialog(requireContext(),R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogCustomBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_custom,
                null,
                false
        )
        cDialog!!.setContentView(bindingDialog.root)
        cDialog!!.setCancelable(false)
        cDialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        //}

        cDialog!!.show()
        bindingDialog.tvTitle.text = getString(R.string.confirmation)
        bindingDialog.tvMsg.text = getString(R.string.do_you_want)

        bindingDialog.btOk.setOnClickListener {
            cDialog!!.dismiss()
            binding!!.nsvMain.visibility = View.GONE
            binding!!.shimmerFrameLayout.visibility = View.VISIBLE
            binding!!.shimmerFrameLayout.startShimmer()
            callMakeListApi("0",file)
        }

        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

        /*val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        builder.setTitle("Confirmation")
        builder.setMessage("Do you want to remove this movie?")
        builder.setPositiveButton("Yes") { dialogInterface, i ->

            callMakeListApi("0",file)

        }
        builder.setNegativeButton("No", null)
        builder.create()
        builder.show()*/
    }




    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val alertMenuItem = menu.findItem(R.id.action_search)
        //val searchView = alertMenuItem!!.actionView as SearchView
        val searchView = SearchView((context as HomeActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myListAdapter!!.filter.filter((newText.toString()))
                return true
            }

        })
        searchView.setOnClickListener {view ->  }

        return super.onCreateOptionsMenu(menu,inflater)
    }*/
    fun getCountryCode(countryName: String): String? {
        val isoCountryCodes = Locale.getISOCountries()
        for (code in isoCountryCodes) {
            val locale = Locale("", code)
            if (countryName.equals(locale.displayCountry, ignoreCase = true)) {
                return code
            }
        }
        return ""
    }
    fun getCountryName(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.networkCountryIso.toUpperCase(Locale.getDefault())
        getCountryCode(Locale("", countryCode).displayCountry)
        return Locale("", countryCode).displayCountry
    }
}


