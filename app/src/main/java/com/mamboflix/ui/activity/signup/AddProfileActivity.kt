package com.mamboflix.ui.activity.signup

import android.Manifest
import android.app.Dialog
import android.content.CursorLoader
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityAddProfileBinding
import com.mamboflix.ui.activity.managedevices.ManageDevicesActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*


class AddProfileActivity : BaseActivity() , View.OnClickListener{
    private var binding: ActivityAddProfileBinding?=null
    private var validation: AwesomeValidation? = null
    private var enterType:Int?=null
    private var cDialog: Dialog? = null
    private var pType:String = "2"
    var isSelectedType:Boolean = false
    private var imageFile: MultipartBody.Part? = null
    private val pickImageCamera = 1
    private val pickImageGallery = 2
    lateinit var currentPhotoPath: String
    var mPhotoFile: File? = null
    var photoURICamera: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_profile)

        clicListenerView()
        initView()
        //myFirebaseToken = FirebaseInstanceId.getInstance().token
        //Log.e("Token fcm", "Token>>$myFirebaseToken")

        enterType = intent.extras!!.getInt("enterType")
        if (enterType==2){
            val userName = intent.extras!!.getString("name")
//            var profileType = intent.extras!!.getString("pro_type")
            val profileImage = intent.extras!!.getString("image")

            binding!!.tvAdd.text = getString(R.string.edit_profile)
            binding!!.edtName.setText(userName)

            if (profileImage!=null){
                Glide.with(this)
                    .load(Uri.parse(profileImage!!))
                    .apply(RequestOptions.placeholderOf(R.drawable.image_loading))
                    .apply(RequestOptions.errorOf(R.drawable.image_loading))
                    .into(binding!!.ivIcon)
            }



//            if (profileType=="1"){
//                binding!!.ivType.setImageResource(R.drawable.uncheck)
//                isSelectedType = false
//                pType = "2"
//            }else{
//                binding!!.ivType.setImageResource(R.drawable.check)
//                isSelectedType = true
//                pType = "1"
//            }
        }

    }



    private fun initView() {
        //This method will use for fetching Token
       /* Thread(Runnable {
            try {
                myFirebaseToken = FirebaseInstanceId.getInstance().token
                Log.e("Token fcm", "Token>>$myFirebaseToken")


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()*/
    }


    fun clicListenerView(){
        binding!!.btnSave.setOnClickListener(this)
        binding!!.btnCancel.setOnClickListener(this)
        binding!!.lytType.setOnClickListener(this)

        binding!!.ivUser.setOnClickListener(this)
        binding!!.ivIcon.setOnClickListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnSave -> {
                if (enterType==1){//If come from from signup
                    if(binding!!.edtName.text.toString().isEmpty()){
                        Toast.makeText(this@AddProfileActivity, "Please enter name.", Toast.LENGTH_SHORT).show()
                    }else{
                        callCreateWatchUserApi(pType)
                    }

                }else{//if come from device management
                    if(binding!!.edtName.text.toString().isEmpty()){
                        Toast.makeText(this@AddProfileActivity, "Please enter name.", Toast.LENGTH_SHORT).show()
                    }else{
                        callEditSubUserApi(pType)
                    }

                }
            }

            R.id.ivUser ->{
                selectImage()

            }
            R.id.ivIcon ->{
                selectImage()
            }

            R.id.btnCancel -> {
               finish()
            }

            R.id.lytType ->{
                pType = if (isSelectedType){
                    binding!!.ivType.setImageResource(R.drawable.uncheck)
                    isSelectedType = false
                    "2"
                }else{
                    binding!!.ivType.setImageResource(R.drawable.check)
                    isSelectedType = true
                    "1"
                }
                //showDialogProfileType(pType)
            }


            else -> {
            }

        }
    }




    private fun callCreateWatchUserApi(mType:String) {
        val userName: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), binding?.edtName?.text.toString())
        val userToken: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"),userPref.getFcmToken().toString())
        val userType: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"),  mType)

        val languageType: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"),  userPref.getPreferredLanguage())

        if(imageFile == null){
            val requestFile =
                    RequestBody.create(MediaType.parse("image/jpg"), "")
            imageFile = MultipartBody.Part.createFormData("image", "", requestFile)
        }

        apiService.callCreateWatchUserApi("Bearer "+userPref.getToken(), imageFile!!,userName,userToken,
            userType,
            languageType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->

                if (commonResponse.status !=0 && commonResponse.mdata != null) {
                    startActivity(Intent(this, ManageDevicesActivity::class.java))
                    finish()
                } else {
                    utils.simpleAlert(
                        this,
                        "Error",
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



    //Edit sub user
    private fun callEditSubUserApi(mType:String) {
        var subUserId = intent.extras!!.getString("sub_id")
        val userSubUserId: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"),  subUserId)
        val userName: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), binding?.edtName?.text.toString())
        val userToken: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"),userPref.getFcmToken().toString())
        val userType: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"),  mType)

        val languageType: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"),  userPref.getPreferredLanguage())


        if(imageFile == null){
            val requestFile =
                    RequestBody.create(MediaType.parse("image/jpg"), "")
            imageFile = MultipartBody.Part.createFormData("profile_image", "", requestFile)
        }

        apiService.callEditSubUserApi("Bearer "+userPref.getToken(),imageFile!!,userSubUserId, userType,userName,userToken,languageType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(this::showProgressDialog)
                .doOnCompleted(this::hideProgressDialog)
                .subscribe({ commonResponse ->
                    if (commonResponse.status !=0) {
                        finish()
                    } else {
                        utils.simpleAlert(
                                this,
                                "Error",
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectImage() {
        val options =
                arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val pm: PackageManager = packageManager
        val builder =
                android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("Select Option")
        builder.setItems(
                options
        ) { dialog: DialogInterface, item: Int ->
            when {
                options[item] == "Take Photo" -> {
                    dialog.dismiss()
                    requestStoragePermission(true)

                }
                options[item] == "Choose From Gallery" -> {
                    dialog.dismiss()

                    requestStoragePermission(false)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageCamera) {
            binding!!.ivIcon.visibility = View.GONE
            binding!!.ivUser.visibility = View.VISIBLE
            binding!!.ivUser.setImageURI(photoURICamera)
            val file = File(currentPhotoPath)
            val requestFile =
                    RequestBody.create(MediaType.parse("image/jpg"), file)
            imageFile = MultipartBody.Part.createFormData("image", file.name, requestFile)
        } else if (requestCode == pickImageGallery && data != null) {
            val selectedImage = data.data
            try {
                binding!!.ivIcon.visibility = View.GONE
                binding!!.ivUser.visibility = View.VISIBLE
                binding!!.ivUser.setImageURI(selectedImage)
                val file = File(getPath(selectedImage!!))
                val requestFile =
                        RequestBody.create(MediaType.parse("image/jpg"), file)
                imageFile =
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri =
                            FileProvider.getUriForFile(
                                    this,
                                    "com.mamboflix.provider",
                                    it
                            )
                    mPhotoFile = photoFile
                    photoURICamera = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, pickImageCamera)
                }
            }
        }

    }



    private fun getPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
                CursorLoader(this, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        Log.d("image path", cursor.getString(column_index))
        return cursor.getString(column_index)
    }



    private fun requestStoragePermission(isCamera: Boolean) {
        val permissions = mutableListOf<String>()
        permissions.add(Manifest.permission.CAMERA)  // Always add CAMERA permission

        // Add media permissions for Android 14 and below
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)  // Read images permission
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)   // Read videos permission
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        // Log to confirm the camera choice
        Log.d("cameratype", isCamera.toString())

        // Request permissions with Dexter
        Dexter.withActivity(this)
            .withPermissions(*permissions.toTypedArray())
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        // If permissions are granted, proceed with camera or gallery
                        if (isCamera) {
                            openCamera()
                        } else {
                            openGallery()
                        }
                        Log.d("cameratype", "permission granted")
                    } else {
                        // If any permission is denied, show an alert or request again
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog() // Navigate to settings to allow permission manually
                        } else {
                            // Optionally, request permissions again if user just denied
                            Log.d("cameratype", "permissions denied")
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest() // Allow user to continue permission request
                }
            })
            .withErrorListener { error ->
                // Handle error if permission request fails
                Toast.makeText(this, "Error occurred while requesting permissions!", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()  // Ensure it runs on the same thread to avoid async issues
            .check()
    }




    private fun openGallery() {
        val pickPhoto =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        this.startActivityForResult(pickPhoto, pickImageGallery)
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings."
        )
        builder.setPositiveButton(
                "GOTO SETTINGS"
        ) { dialog: DialogInterface, which: Int ->
            openSettings()
            dialog.cancel()
        }
        builder.setNegativeButton(
                "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.mamboflix", null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

}
