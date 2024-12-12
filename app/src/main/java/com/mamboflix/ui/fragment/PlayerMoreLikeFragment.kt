package com.mamboflix.ui.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mamboflix.BaseFragment
import com.mamboflix.R
import com.mamboflix.databinding.FragmentMoreLikeBinding
import com.mamboflix.model.contentdetails.*
import com.mamboflix.ui.adapter.relatedcontent.RelatedContentAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException


@Suppress("UNREACHABLE_CODE")
class PlayerMoreLikeFragment : BaseFragment(), View.OnClickListener {
    private var binding: FragmentMoreLikeBinding? =null
    private var myListAdapter: RelatedContentAdapter? = null
    private var moreLikeAdaper: MoreLikeContantAdapter? = null
    var myList : ArrayList<RelatedContentDetailsModel>? = null
    var episode_list : ArrayList<EpisodeData>? = null
    var morelikelis_list : ArrayList<MoreLikeThisData>? = null
    var mySeasionList : ArrayList<SeasionListModel>? = null
    private var content_Id: String?=null
    private var episode_Id: String?=null
    private  var relatedId: String?=null
    private  var isEpisode: String?=null
    private var listener: OnItemMoreClickListener? = null
    private var adapterSeasion: ArrayAdapter<SeasionListModel>? = null

    companion object : OnItemMoreClickListener {
        // Method for creating new instances of the fragment
        fun newInstance(content_Id: String, /*episodeId: String,*/ relatedId: String,isEpisode: String): PlayerMoreLikeFragment {
            // Store the movie data in a Bundle object
            val args = Bundle()
            args.putString("contentId", content_Id)
            //args.putString("episodeId",episodeId)
            args.putString("relatedId",relatedId)
            args.putString("isEpisode",isEpisode)
            // Create a new MovieFragment and set the Bundle as the arguments
            // to be retrieved and displayed when the view is created
            val fragment = PlayerMoreLikeFragment()
            fragment.setOnItemClickListener(this)
            fragment.arguments = args
            return fragment
        }
        override fun onItemClick(contentId: String, episodeId: String) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myList = ArrayList()
        mySeasionList = ArrayList()
        getBundle()
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_like, container, false)

        setHasOptionsMenu(true)
        return binding!!.root
    }

    private fun getBundle(){
        val bundle = this.arguments
        if (bundle != null) {
            content_Id = bundle.getString("contentId")
            //episode_Id = bundle.getString("episodeId")
            relatedId = bundle.getString("relatedId")
            isEpisode = bundle.getString("isEpisode")
            //Log.e("related_id", "<<relate content >> $relatedId")
            callAccessRelatedAPI("",true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as HomeActivity).changeIcon(false)
        //(requireActivity() as HomeActivity).changeIcon(true)
        //(requireActivity() as HomeActivity).fragmenttitle("Home", true)

        if(isEpisode=="yes"){
            binding!!.lySpSeason.visibility = View.VISIBLE
        }else{
            binding!!.lySpSeason.visibility = View.GONE

        }


        //setSpinner()
//        setRecyclerview()
        setSpinner(mySeasionList!!)
    }

    private fun setSpinner(mdata: ArrayList<SeasionListModel>){
        adapterSeasion = ArrayAdapter(
            requireContext(), // Context
            android.R.layout.simple_spinner_item, // Layout
            mdata // Array
        )
        adapterSeasion!!.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        // Finally, data bind the spinner object with Adapter
        binding!!.spSeason.adapter = adapterSeasion
         binding!!.spSeason.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
             override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                 //Toast.makeText(this,position.toString(),Toast.LENGTH_SHORT).show()

                 callAccessRelatedAPI(mdata!![position].seasion_id,false)
                 //Log.e("position",position.toString())
                 if(position == 0){
                     //binding.etAmount.setText("")
                 } else {
                     //binding.etAmount.setText(planList[position].price)
                 }
             }
             override fun onNothingSelected(parent: AdapterView<*>){
                 // Another interface callback
             }
         }

        // list of spinner items
       /* val list = listOf(
            "Honeydew",
            "Laurel green",
            "Light salmon",
            "Bright maroon",
            "Mango Tango",
            "Moss green",
            "Antique bronze"
        )

        // initialize an array adapter for spinner
        val adapter: ArrayAdapter<String> = object: ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            list
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text size
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP,16F)

                // set selected item style
                if (position == binding!!.spSeason.selectedItemPosition){
                    view.background = ColorDrawable(Color.parseColor("#FFF600"))
                    view.setTextColor(Color.parseColor("#2E2D88"))
                }

                return view
            }
        }

        // finally, data bind spinner with adapter
        binding!!.spSeason.adapter = adapter

        // set up initial selection
        binding!!.spSeason.setSelection(0)*/
    }


    private fun setRecyclerview() {
        myListAdapter = RelatedContentAdapter(requireContext(),episode_list!!)
        val linearLayoutManager =
         LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding!!.rvMyList.layoutManager = linearLayoutManager
        binding!!.rvMyList.adapter = myListAdapter

        myListAdapter!!.setOnItemClickListener(object : RelatedContentAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View) {
                if (myList!![position].content_type=="1"){
                    setDataListener(myList!![position].id,"")

                }else{
                    setDataListener(myList!![position].content_id,myList!![position].id)
                }
            }
        })
    }
    private fun setRecyclerviewMoreLike() {
        moreLikeAdaper = MoreLikeContantAdapter(requireContext(),morelikelis_list!!)
        val linearLayoutManager =
         LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding!!.rvMoreLikeList.layoutManager = linearLayoutManager
        binding!!.rvMoreLikeList.adapter = myListAdapter

        moreLikeAdaper!!.setOnItemClickListener(object : MoreLikeContantAdapter.OnItemClickListenerMoreLike {
            @SuppressLint("LogNotTimber")
            override fun onItemClickMoreLike(position: Int, view: View) {
                if (morelikelis_list!![position].content_type==1){
                    setDataListener(morelikelis_list!![position].id.toString(),"")

                }else{
                    setDataListener(morelikelis_list!![position].related_content_id.toString(),morelikelis_list!![position].id.toString())
                }
            }

        })
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("my test", "onAttach")
        if (context is OnItemMoreClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    fun setOnItemClickListener(listener: OnItemMoreClickListener) {
        this.listener = listener
    }

    interface OnItemMoreClickListener {
        fun onItemClick(contentId: String, episodeId: String)
    }

    private fun setDataListener(content_Id: String, episode_Id: String) {
        if (listener != null) {
            listener!!.onItemClick(content_Id!!,episode_Id!!)
        }
    }

    private fun callAccessRelatedAPI(seasionId:String, isFirst:Boolean) {
        //binding!!.lySpSeason.visibility=View.GONE
        if (isFirst){
            myList!!.clear()
            mySeasionList!!.clear()
        }else{
            myList!!.clear()
        }

        apiService.callAccessRelatedAPI("Bearer "+userPref.getToken(),relatedId.toString(),
            userPref.getFcmToken().toString(),
            content_Id.toString(),seasionId,
            userPref.getPreferredLanguage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /*.doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)*/
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0 && commonResponse.mdata != null) {
                        if (isFirst) {
                            if (commonResponse.mdata.datas != null && commonResponse.mdata.datas.size > 0) {
                                myList!!.addAll(commonResponse.mdata.datas)
                                binding!!.rvMyList.visibility=View.VISIBLE
                                myListAdapter!!.notifyDataSetChanged()
                            }
                            if (commonResponse.mdata.seasion_list != null && commonResponse.mdata.seasion_list.size > 0) {
                                // setSpinner(commonResponse.mdata.seasion_list)
                                binding!!.lySpSeason.visibility=View.VISIBLE
                                mySeasionList!!.addAll(commonResponse.mdata.seasion_list)
                                adapterSeasion!!.notifyDataSetChanged()
                            }/*else{
                                binding!!.lySpSeason.visibility=View.GONE
                            }*/
                        }else{
                            if (commonResponse.mdata.datas != null && commonResponse.mdata.datas.size > 0) {
                                binding!!.rvMyList.visibility=View.VISIBLE
                                myList!!.addAll(commonResponse.mdata.datas)
                                myListAdapter!!.notifyDataSetChanged()
                            }else if(commonResponse.mdata.seasion_list.size == 0 ){
                                binding!!.lySpSeason.visibility=View.GONE
                                binding!!.rvMyList.visibility=View.GONE
                            }
                        }
                        //adapterSeasion
                    } else {
                        if(isFirst){
                            binding!!.lySpSeason.visibility=View.GONE
                        }
                        binding!!.rvMyList.visibility=View.GONE
                        myListAdapter!!.notifyDataSetChanged()
                      /*  utils.simpleAlert(
                                requireContext(),
                                "Alert",
                                "No Data"
                        )*/
                        //hideProgressDialog()
                    }
                }, { throwable ->
                    //hideProgressDialog()
                    if (throwable is ConnectException) {
//                        utils.simpleAlert(
//                                requireContext(),
//                                "Error",
//                                getString(R.string.check_network_connection)
//                        )
                    } else {
//                        utils.simpleAlert(
//                            requireContext(),
//                            "Error",
//                            throwable.message.toString())
                       /* utils.simpleAlert(
                                requireContext(),
                                "Error",
                                getString(R.string.check_network_connection))*/
                    }
                })
    }




    //inflate the menu
   /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notification, menu)
        //super.onCreateOptionsMenu(menu, inflater)
        val alertMenuItem = menu.findItem(R.id.activity_main_alerts_menu_item)
        val rootView = alertMenuItem!!.actionView as FrameLayout
        countTextView = rootView.findViewById(R.id.view_alert_count_textview)

        rootView.setOnClickListener {
            val fragment = NotificationFragment()
            CommonUtils.setFragment(fragment, false, requireActivity(), R.id.frameContainer)
        }

                //Log.e("notification","<<onCreateOptionsMenu Count>> "+notificationCount)

        countTextView!!.text = notificationCount

        return super.onCreateOptionsMenu(menu,inflater)
    }*/


   /* override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notification, menu)
        val alertMenuItem = menu.findItem(R.id.activity_main_alerts_menu_item)
        val rootView = alertMenuItem!!.actionView as FrameLayout
        countTextView = rootView.findViewById(R.id.view_alert_count_textview)
        *//*redCircle = rootView.findViewById(R.id.view_alert_red_circle)
        countTextView = rootView.findViewById(R.id.view_alert_count_textview)*//*
        //  countTextView!!.setText(Utils.isNotification)

        Log.e("notification","<<onCreateOptionsMenu Count>> "+notificationCount)

        countTextView!!.text = notificationCount


        return super.onCreateOptionsMenu(menu,inflater)
    }*/

}


