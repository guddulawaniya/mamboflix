package com.mamboflix.ui.activity.managedevices


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.mamboflix.BaseActivity
import com.mamboflix.HomeActivity3.HomeActivity3
import com.mamboflix.R
import com.mamboflix.databinding.*
import com.mamboflix.model.CreateWatchUserModel
import com.mamboflix.model.managedevice.DeviceModel
import com.mamboflix.ui.activity.signup.AddProfileActivity
import com.mamboflix.ui.adapter.devicemanage.ManageDeviceAdapter
import com.mamboflix.ui.adapter.devicemanage.ManageUserAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException

@Suppress("UNREACHABLE_CODE")
class ManageDevicesActivity : BaseActivity(), View.OnClickListener {
    private var binding: ActivityManageDevicesBinding? =null
    private var id:String?=""
    private var name:String?=""
    private var manageDeviceAdapter: ManageDeviceAdapter? = null
    private var manageUserAdapter: ManageUserAdapter? = null
    var devicesList : ArrayList<DeviceModel>? = null
    var userList : ArrayList<CreateWatchUserModel>? = null
    private var cDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_manage_devices)
        setToolBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        devicesList = ArrayList()
        userList = ArrayList()
        //callGetWatchingUserAPI()
        setRecyclerview()
    }

    private fun setRecyclerview() {
        binding!!.lytAddProfile.setOnClickListener(this)
        manageDeviceAdapter = ManageDeviceAdapter(this,devicesList!!)
        val linearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.rvMyList.layoutManager = linearLayoutManager
        binding!!.rvMyList.adapter = manageDeviceAdapter

        manageDeviceAdapter!!.setOnItemClickListener(object : ManageDeviceAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(position: Int, view: View) {

                when (view?.id) {
                    R.id.ivDelete ->{
                        showDialogDeviceDelete( "Do you want delete "+devicesList!![position].device_name,"Alert",devicesList!![position].device_id,1,position)
                    }
                    R.id.ivEdit ->{
                        showDialogDeviceEdit("Update Device",devicesList!![position].device_id,devicesList!![position].device_name)
                    }
                }
            }
        })

        manageUserAdapter = ManageUserAdapter(this,userList!!, userPref.getSubUserId().toString())
        val linearLayoutManager1 =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding!!.rvManageUser.layoutManager = linearLayoutManager1
        binding!!.rvManageUser.adapter = manageUserAdapter
        manageUserAdapter!!.setOnItemClickListener(object : ManageUserAdapter.OnItemClickListener {
            @SuppressLint("LogNotTimber")
            override fun onItemClick(id:String,name:String,position: Int, view: View) {
                when (view?.id) {
                    R.id.ivDelete ->{
                        showDialogDeviceDelete("Do you want delete "+userList!![position].name,"Alert",userList!![position].id,2,position)
                    }
                    R.id.ivEdit ->{
                        sendNext(position)
                    }
                    R.id.lytChats ->{
                        userPref.setSubUserId(userList!![position].id)
                        userPref.setSubUserName(userList!![position].name)
                        userPref.setsubuserImg(userList!![position].image)
                        startActivity(Intent(this@ManageDevicesActivity,HomeActivity3::class.java)
                            .setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY))
//                        callswitchProfile(id,name)
                    }

                }
              /*  startActivity(Intent(this, AddProfileActivity::class.java)
                        .putExtra("enterType",1))*/
            }
        })
    }
    private fun sendNext(pos:Int){
        startActivity(Intent(this, AddProfileActivity::class.java)
                .putExtra("enterType",2)
                .putExtra("pro_type", userList!![pos].type)
                .putExtra("image",userList!![pos].image)
                .putExtra("sub_id", userList!![pos ].id)
                .putExtra("name",userList!![pos].name))
        }

    private fun setToolBar(){
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding!!.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        setTitle(resources.getString(R.string.manageDevices))
       // binding!!.toolbar.title= resources.getString(R.string.manageDevices)
        //title = "Manage Devices"
    }
    override fun onResume() {
        super.onResume()
       /* setRecyclerview()*/
        callGetWatchingUserAPI()
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lytAddProfile ->{
                startActivity(Intent(this, AddProfileActivity::class.java)
                        .putExtra("enterType",1))
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity3::class.java).putExtra("NEW_MAMBER","NEW MAMBER"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callswitchProfile(id: String,name:String) {
        apiService.switch_profile(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                try {
                    if (commonResponse.status != 0 && commonResponse.mdata != null) {
                        userPref.setSubUserId(id)
                        userPref.setSubUserName(name)
                        startActivity(Intent(this@ManageDevicesActivity,HomeActivity3::class.java)
                            .setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY))
                    } else {

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, { throwable ->

            })
        }


   private fun callGetWatchingUserAPI() {
       devicesList!!.clear()
       userList!!.clear()
       apiService.callGetWatchingUserAPI("Bearer "+userPref.getToken(),userPref.getFcmToken().toString(),
           userPref.getPreferredLanguage())
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .doOnSubscribe(this::showProgressDialog)
           .doOnCompleted(this::hideProgressDialog)
           .subscribe({ commonResponse ->
               if (commonResponse.status !=0 && commonResponse.mdata != null) {
                   if (commonResponse.mdata.devices !=null && commonResponse.mdata.devices.size>0){
                       devicesList!!.addAll(commonResponse.mdata.devices)
                   }

                   manageDeviceAdapter!!.notifyDataSetChanged()
                   if (commonResponse.mdata.sub_users !=null && commonResponse.mdata.sub_users.size>0){
                       userList!!.addAll(commonResponse.mdata.sub_users)
                       for (i in 0 until commonResponse.mdata.sub_users.size){
                            id = commonResponse.mdata.sub_users[i].id.toString()
                            name = commonResponse.mdata.sub_users[i].name.toString()
//                           userPref.setSubUserId(id)
//                           userPref.setSubUserName(name)

                       }
                   }
                   manageUserAdapter!!.notifyDataSetChanged()
               } else {
                   utils.simpleAlert(
                       this,
                       "",
                       commonResponse.message.toString()
                   )
                   hideProgressDialog()
               }
           }, { throwable ->
               hideProgressDialog()
               if (throwable is ConnectException) {
                   utils.simpleAlert(
                       this,
                       "Error",
                       getString(R.string.check_network_connection)
                   )

               } else {
                   utils.simpleAlert(
                       this,
                       "Error",
                       throwable.message.toString())

               }

           })
   }

    private fun showDialogDeviceEdit(topTitle:String,deviceId:String,deviceName: String ) {
        cDialog = Dialog(this,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogDeviceEditBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_device_edit,
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
        bindingDialog.tvTitle1.text = topTitle
        bindingDialog.etProfile.setText(deviceName)
        bindingDialog.tvTitle1.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

        bindingDialog.btSubmit.setOnClickListener {

            if (bindingDialog.etProfile.text.toString().isNotEmpty()){
                cDialog!!.dismiss()
                callEditDeviceApi(deviceId,bindingDialog.etProfile.text.toString())
            }else{
                utils.simpleAlert(this,"Error", "Please enter device name")
            }

        }
    }

    private fun showDialogDeviceDelete(title_two:String,topTitle:String,deviceId:String, mType:Int,pos:Int) {
        cDialog = Dialog(this,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogShowBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_show,
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
        bindingDialog.tvTitle1.text = topTitle
        bindingDialog.tvTitle2.text = title_two
        bindingDialog.tvTitle1.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
         bindingDialog.btCancel.setOnClickListener {
             cDialog!!.dismiss()
         }

        bindingDialog.btSubmit.setOnClickListener {
            cDialog!!.dismiss()
            if(mType==1){
                callDeleteParticularDeviceApi(deviceId, devicesList!![pos])
            }else{
                callDeleteSubUserApi(deviceId,userList!![pos])
            }



        }
    }

    private fun showDialogDeviceEdit(title_two:String,topTitle:String,deviceId:String, mType:Int) {
        cDialog = Dialog(this,R.style.Theme_Tasker_Dialog)
        val bindingDialog: DialogShowBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.dialog_show_edit,
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
        bindingDialog.tvTitle1.text = topTitle
        bindingDialog.tvTitle2.text = title_two
        bindingDialog.tvTitle1.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        bindingDialog.btCancel.setOnClickListener {
            cDialog!!.dismiss()
        }

        bindingDialog.btSubmit.setOnClickListener {
            cDialog!!.dismiss()
            if(mType==1){
                //callDeleteParticularDeviceApi(deviceId)
            }else{
                //callDeleteSubUserApi(deviceId)
            }

        }
    }

    //callDeleteParticularDeviceApi
    private fun callDeleteParticularDeviceApi(device_id:String, mList:DeviceModel) {
        apiService.callDeleteParticularDeviceApi("Bearer "+userPref.getToken(),device_id,userPref.getFcmToken().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0) {
                        devicesList!!.remove(mList)
                        manageDeviceAdapter!!.notifyDataSetChanged()
                      /*  utils.simpleAlert(
                            this,
                            "",
                            "Successfully Deleted"
                        )
*/
                    //    Toast.makeText(this,""+ commonResponse.message.toString(), Toast.LENGTH_LONG).show()

                    } else {
                        utils.simpleAlert(
                                this,
                                "",
                                commonResponse.message.toString()
                        )
                        hideProgressDialog()
                    }
                }, { throwable ->
                    hideProgressDialog()
                    if (throwable is ConnectException) {
                        utils.simpleAlert(
                                this,
                                "Error",
                                getString(R.string.check_network_connection)
                        )

                    } else {

                        utils.simpleAlert(
                                this,
                                "Error",
                               throwable.message.toString())

                    }

                })
    }

    //callDeleteSubUserApi
    private fun callDeleteSubUserApi(device_id:String,mList: CreateWatchUserModel) {
        apiService.callDeleteSubUserApi("Bearer "+userPref.getToken(),device_id,userPref.getFcmToken().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0 ) {
                        userList!!.remove(mList)
                        manageUserAdapter!!.notifyDataSetChanged()
                        if (userList!!.size>0){
                            if (mList.id==userPref.getSubUserId()){
                                userPref.setSubUserId(userList!![0].id)
                                userPref.setSubUserName(userList!![0].name)
                            }
                        }else{
                            userPref.setSubUserId("")
                            userPref.setSubUserName("")
                        }
                     /*   utils.simpleAlert(
                            this,
                            "",
                           "Successfully Deleted"
                        )*/
                    //    Toast.makeText(this,""+ commonResponse.message.toString(), Toast.LENGTH_LONG).show()

                    } else {
                        utils.simpleAlert(
                                this,
                                "",
                                commonResponse.message.toString()
                        )
                        hideProgressDialog()
                    }
                }, { throwable ->
                    hideProgressDialog()
                    if (throwable is ConnectException) {
                        utils.simpleAlert(
                                this,
                                "Error",
                                getString(R.string.check_network_connection)
                        )

                    } else {
                        utils.simpleAlert(
                                this,
                                "Error",
                               throwable.message.toString())

                    }
                })
    }





    //callEditDeviceApi

    private fun callEditDeviceApi(device_id:String,deviceName:String) {
        devicesList!!.clear()
        apiService.callEditDeviceApi("Bearer "+userPref.getToken(),deviceName,device_id,userPref.getFcmToken().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0 && commonResponse.mdata != null) {
                        devicesList!!.addAll(commonResponse.mdata.devices)
                        manageDeviceAdapter!!.notifyDataSetChanged()
                    } else {
                        utils.simpleAlert(
                                this,
                                "",
                                commonResponse.message.toString()
                        )
                        hideProgressDialog()
                    }
                }, { throwable ->
                    hideProgressDialog()
                    if (throwable is ConnectException) {
                        utils.simpleAlert(
                                this,
                                "Error",
                                getString(R.string.check_network_connection)
                        )

                    } else {
                        utils.simpleAlert(
                                this,
                                "Error",
                                throwable.message.toString())

                    }
                })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,HomeActivity3::class.java))
    }


}


