package com.mamboflix.ui.activity.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.CursorLoader
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityEditProfileBinding
import com.mamboflix.prefs.UserPref
import com.mamboflix.utils.CommonUtils
import com.mamboflix.utils.Constants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditProfileBinding
    private var datePickerDialog: DatePickerDialog? = null
    private var validation: AwesomeValidation? = null
    var year1: String? = null
    var month1: String? = null
    var day1: String? = null
    val ALL_PERMISSIONS = 22
    val PICK_IMAGE_GALLERY = 2
    val PICK_IMAGE_CAMERA = 3
    var mImageFile: File? = null
    var imgPath: String = ""
    private var imageFile: MultipartBody.Part? = null
    private val pickImageCamera = 1
    private val pickImageGallery = 2
    lateinit var currentPhotoPath: String
    var mPhotoFile: File? = null
    var photoURICamera: Uri? = null
    lateinit var userPref1: UserPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        getUserProfileDetail()
        setToolBar()
        initializeValidation()
        userPref1 = UserPref(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setValue()
        val tm: TelephonyManager =
            this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue: String = tm.networkCountryIso
        //  Toast.makeText(this,countryCodeValue.toUpperCase(),Toast.LENGTH_LONG).show()
        binding.ccp.setCountryForNameCode(countryCodeValue.toUpperCase())
    }

    private fun setValue() {

        binding.edtName.setText(userPref.user.name)
        binding.edtMobile.setText(userPref.user.mobile)
        binding.edtEmail.setText(userPref.user.email)

        Glide.with(this).load(userPref.getsubuserImg())
            .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
            .apply(RequestOptions.errorOf(R.drawable.user_profile))
            .into(binding.ivIcon)

//        if (!userPref.getsubuserImg().isNullOrBlank()) {
//            Glide.with(this).load(Uri.parse(userPref.getsubuserImg()))
//                .into(binding.ivIcon)
//        }
//        if (!userPref.user.profile_image.isNullOrBlank()) {
//            Glide.with(this).load(Uri.parse(userPref.user.profile_image))
//                .into(binding.ivIcon)
//        }
    }


    private fun initializeValidation() {

        binding.btnUpdate.setOnClickListener(this)
        binding.ivUser.setOnClickListener(this)
        binding.ivIcon.setOnClickListener(this)
        validation = AwesomeValidation(ValidationStyle.BASIC)
        validation!!.addValidation(
            binding.edtName,
            Constants.EMPTY_REGEX,
            "Please enter the valid Name"
        )


        validation!!.addValidation(
            binding.edtEmail,
            Patterns.EMAIL_ADDRESS,
            "Please enter the valid Email"
        )

        validation!!.addValidation(
            binding.edtMobile,
            Constants.EMPTY_REGEX,
            "Please enter the valid Mobile Number"
        )


    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Edit Profile "
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.ivUser -> {
                selectImagesCameraPermission()
            }
            R.id.ivIcon -> {
                selectImagesCameraPermission()
            }
            R.id.btnUpdate -> {
                if (validation!!.validate()) {
                    if (binding.edtMobile.text.toString().length < 10) {
                        Toast.makeText(
                            applicationContext,
                            "Please enter valid number",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        callUpdateUserAPI()
                    }


                    //finish()
                }
            }
        }

        /* when(view!!.id){
             R.id.btn_phone -> {

                 if (validation!!.validate()) {
                     utils.hideKeyboard(binding!!.edtPhone)
                     callloginAPI()
                 }
             }

             R.id.tvSignup -> {
                 startActivity(Intent(this, MobileActivity::class.java))
             }

             R.id.tvForgot -> {
                 //startActivity(Intent(this, ForgotPasswordActivity::class.java))

             }


         }*/
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectImagesCameraPermission() {
        val permissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )


        // Check for media permissions based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        // Check if permissions are already granted
        val isCameraGranted = CommonUtils.isCameraGranted(this@EditProfileActivity)
        val isReadStorageGranted = CommonUtils.isReadStorageGranted(this@EditProfileActivity)

        when {

            isCameraGranted -> {
                Log.d("editprofileimage","fdhdhfghgfhgfhfg")
                // Both permissions are granted, proceed to select image
                selectImage()
            }
            !isReadStorageGranted -> {
                Log.d("editprofileimage","fdhdhfghgfhgfhfg")
                // Request permissions if read storage is not granted
                request_Permissions(this@EditProfileActivity, 1, *permissions.toTypedArray())
            }
            !isCameraGranted -> {
                Log.d("editprofileimage","fdhdhfghgfhgfhfgggggg")
                // Request permissions if camera is not granted
                request_Permissions(this@EditProfileActivity, 2, *permissions.toTypedArray())
            }
        }
    }


    private fun callUpdateUserAPI() {
        val userName: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), binding.edtName.text.toString())
        val userEmail: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), binding.edtEmail.text.toString())
        val userPhone: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), binding.edtMobile.text.toString())

        val country_code: RequestBody = RequestBody.create(MediaType.parse("text/plain"), binding.ccp.selectedCountryCode.toString())

        if (imageFile == null) {
            val requestFile = RequestBody.create(MediaType.parse("image/jpg"), "")
            imageFile = MultipartBody.Part.createFormData("profile_image", "", requestFile)
        }

        apiService.callUpdateUserAPI(
            "Bearer " + userPref.getToken(),
            imageFile!!, userName, country_code, userEmail, userPhone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status == 1) {
                    imageFile = null
                    userPref.user = commonResponse.mdata!!.user
                    userPref.setsubuserImg(commonResponse.mdata.user.profile_image)
                    userPref.setSubUserName(commonResponse.mdata.user.name)
                    Glide.with(this).load(userPref.getsubuserImg())
                        .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
                        .apply(RequestOptions.errorOf(R.drawable.user_profile))
                        .into(binding.ivIcon)
                    Log.d("TAG", "callUpdateUserAPI: " + commonResponse.mdata.user.profile_image)
//                    Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
                    //startActivity(Intent(this, HomeActivity::class.java))
                    //finish()
                    onBackPressed()

                } else {
                    utils.simpleAlert(
                        this,
                        "",
                        commonResponse.message.toString()
                    )

//                    finish()
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
                        throwable.message.toString()
                    )


                }

            })
    }


    /* private fun requestStoragePermission(isCamera: Boolean) {
         Dexter.withActivity(this)
             .withPermissions(
                 Manifest.permission.READ_EXTERNAL_STORAGE,
                 Manifest.permission.WRITE_EXTERNAL_STORAGE,
                 Manifest.permission.CAMERA
             )
             .withListener(object : MultiplePermissionsListener {
                 override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                     // check if all permissions are granted
                     if (report.areAllPermissionsGranted()) {
                         if (isCamera) {
                             openCamera()
                         } else {
                             openGallery()
                         }
                     }
                     // check for permanent denial of any permission
                     if (report.isAnyPermissionPermanentlyDenied) {
                         // show alert dialog navigating to Settings
                         showSettingsDialog()
                     }
                 }*/


    /* private fun openCamera() {
         val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         this.startActivityForResult(intent, pickImageCamera)
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
     }*/


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun request_Permissions(
        activity: Activity?,
        permissionId: Int,
        vararg permissions: String?) {
        requestPermissions(permissions, permissionId)
    }


    private fun requestPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(
            this,
            permissions,
            ALL_PERMISSIONS
        )
//  requestPermissions(permissions, ALL_PERMISSIONS)
    }


    private fun selectImage() {

        Log.d("editprofileimage","fdhdhfghgfhgfhfg")
        try {
            val pm: PackageManager = this.packageManager
            val hasPerm = pm.checkPermission(
                Manifest.permission.CAMERA,
                this.packageName.toString()
            )
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
                val builder = android.app.AlertDialog.Builder(this@EditProfileActivity)

                builder.setItems(
                    options
                ) { dialog: DialogInterface, item: Int ->
                    if (options[item] == "Take Photo") {
                        dialog.dismiss()
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        mImageFile = CommonUtils.createImageFile(this)

                        cameraIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(
                                this,
                                this.packageName + ".provider",
                                mImageFile!!
                            )
                        )
                        startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA)
                    } else if (options[item] == "Choose From Gallery") {
                        dialog.dismiss()
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
                    } else if (options[item] == "Cancel") {
                        dialog.dismiss()
                    }
                }
                builder.show()
            } else requestPermission()
            // Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (e: Exception) {
            Log.e("TAG1111", "selectImage: " + e.message)
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_CAMERA -> handleCameraResult()
                PICK_IMAGE_GALLERY -> handleGalleryResult(data)
            }
        } else {
            Log.d("TAG", "Activity result canceled.")
        }
    }

    private fun handleCameraResult() {
        mImageFile?.let { imageFile ->
            try {
                Log.d("TAG", "Processing camera image")

                // Convert the File to a URI
                val imageUri = imageFile.toUri()

                // Use Glide to load the image into the CircleImageView
                Glide.with(this)
                    .load(imageUri) // Load the image from the URI
                    .apply(RequestOptions.circleCropTransform()) // Apply circular crop
                    .into(binding.ivIcon) // Set the image in the CircleImageView

                // Additional processing can go here if needed

                Log.d("cameraimageurl", "$imageFile.toString()")
                // Prepare file for upload
                prepareImageForUpload(imageFile.toString())

            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage ?: "Unknown error")
            }
        } ?: Log.e("Error", "mImageFile is null")
    }




    private fun handleGalleryResult(data: Intent?) {
        data?.data?.let { selectedImage ->
            try {
                Log.d("TAG", "Processing gallery image")

                // Retrieve the Bitmap from the URI
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage) ?: run {
                    Log.e("errorPrint", "Failed to get bitmap from gallery URI.")
                    return
                }
                // Change image orientation and compress
                val newBitmap = CommonUtils.changeImageOrientation(
                    CommonUtils.getRealPathFromUri(this, selectedImage)?.absolutePath ?: return,
                    bitmap
                ) ?: return

                imgPath = CommonUtils.compressImage(
                    CommonUtils.getRealPathFromUri(this, selectedImage)?.absolutePath ?: return
                ).toString()

                binding.ivIcon.setImageBitmap(newBitmap)


                // Prepare file for upload
                prepareImageForUpload(imgPath)
            } catch (e: Exception) {
                Log.e("errorPrint", e.localizedMessage ?: "Unknown error")
            }
        } ?: Log.e("errorPrint", "Selected image URI is null")
    }

    private fun prepareImageForUpload(imagePath: String) {
        if (imagePath.isNotEmpty()) {
            val fileToUpload = File(imagePath)
            val requestFile = RequestBody.create(MediaType.parse("image/jpg"), fileToUpload)

            imageFile = MultipartBody.Part.createFormData(
                "profile_image",
                fileToUpload.name,
                requestFile
            )
        } else {
            Log.e("errorPrint", "Image path is empty.")
        }
    }



    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        if (isCamera) {
                            openCamera()
                        } else {
                            openGallery()
                        }
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            })
            .withErrorListener { error ->
//                Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT)
//                    .show()
            }
            .onSameThread()
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

    private fun getUserProfileDetail() {
        apiService.userData("Bearer " + userPref.getToken(), userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ commonResponse ->

                if (commonResponse.status != 0 && commonResponse.user_data != null) {


                    Glide.with(this).load(Uri.parse(commonResponse.user_data.profile_image))
                        .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
                        .apply(RequestOptions.errorOf(R.drawable.user_profile))
                        .into(binding.ivIcon)

//                    Toast.makeText(this, " success ", Toast.LENGTH_SHORT).show()
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {

                } else {
                }

            })
    }


}
