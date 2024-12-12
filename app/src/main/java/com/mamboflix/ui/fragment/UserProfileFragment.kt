package com.mamboflix.ui.fragment


import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import android.content.CursorLoader
import com.mamboflix.BaseFragment
import okhttp3.MediaType
import com.mamboflix.R
import com.mamboflix.databinding.FragmentUserProfileBinding
import okhttp3.MultipartBody
import android.content.pm.PackageManager
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentUserProfileBinding
    private var imageFile: MultipartBody.Part? = null
    private val PICK_IMAGE_CAMERA = 1
    private val PICK_IMAGE_GALLERY = 2



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_profile, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        //   (activity as HomeActivity).changeIcon(false)
        //setHasOptionsMenu(true)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setListener()
        setUserData()
        return binding.root
    }

    private fun setListener() {
       /* binding.btnUpdateProfile.setOnClickListener(this)
        binding.btnUpdatePass.setOnClickListener(this)
        binding.ivUserImage.setOnClickListener(this)*/
    }

    fun setUserData(){
        /*binding.etName!!.setText(userPref.user.full_name)
        binding.etEmail!!.setText(userPref.user.email)
        binding.etPhone!!.setText(userPref.user.telephone)
        //binding.edtQulification!!.setText(userPref.user.qualification)
        binding.edtRemark!!.setText(userPref.user.study_status)

        binding.tvReferral.text = userPref.user.user_id

        if (!userPref.user.ref_id.isNullOrBlank()){
            binding.tvSponsor.text = userPref.user.ref_id
        }

        Glide.with(this).load(Uri.parse(userPref.user.image))
            .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
            .apply(RequestOptions.errorOf(R.drawable.placeholder))
            .into(binding.ivIcon)*/


    }



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(requireActivity() as HomeActivity).changeIcon(false)
        //(requireActivity() as HomeActivity).fragmenttitle("Update Profile", false)
    }

   /* private fun setData(data: UserModel) {
        binding.etName.setText(data.full_name)
        binding.etEmail.setText(data.email)
        binding.etPhone.setText(data.telephone)




        Glide.with(this).load(Uri.parse(data.image))
            .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
            .apply(RequestOptions.errorOf(R.drawable.placeholder))
            .into(binding.ivIcon)


    }*/

    override fun onClick(v: View?) {
        when (v?.id) {
        }

    }

   /* private fun validatePin(): Boolean {

        if (binding.etOldPass.text.isEmpty() || binding.etOldPass.text.length < 4){
            binding.etOldPass.error="Please enter valid old password"
            return false
        }else if(binding.etNewPass.text.isEmpty() || binding.etNewPass.text.length < 4){
            binding.etNewPass.error ="Please enter valid new password"
            return false
        }
        return true

    }*/


    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val pm: PackageManager = context!!.packageManager
        val builder =
            AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle("Select Option")
        builder.setItems(
            options
        ) { dialog: DialogInterface, item: Int ->
            if (options[item] == "Take Photo") {
                dialog.dismiss()
                val cameraPermission = pm.checkPermission(
                    Manifest.permission.CAMERA,
                    context!!.packageName
                )
                val storagePermission = pm.checkPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    context!!.packageName
                )
                if (cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    this.startActivityForResult(intent, PICK_IMAGE_CAMERA)
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 1
                    )
                }
            } else if (options[item] == "Choose From Gallery") {
                dialog.dismiss()
                val hasPerm = pm.checkPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    context!!.packageName
                )
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    this.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        2
                    )
                }
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun selectImage121() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val pm: PackageManager = context!!.packageManager
        val builder =
            AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle("Select Option")
        builder.setItems(
            options
        ) { dialog: DialogInterface, item: Int ->
            if (options[item] == "Take Photo") {
                dialog.dismiss()
                val cameraPermission = pm.checkPermission(
                    Manifest.permission.CAMERA,
                    context!!.packageName
                )
                val storagePermission = pm.checkPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    context!!.packageName
                )
                if (cameraPermission == PackageManager.PERMISSION_GRANTED && storagePermission == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    this.startActivityForResult(intent, PICK_IMAGE_CAMERA)
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 1
                    )
                }
            } else if (options[item] == "Choose From Gallery") {
                dialog.dismiss()
                val hasPerm = pm.checkPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    context!!.packageName
                )
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    this.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        2
                    )
                }
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CAMERA && data != null) {
            val bitmap = data.extras!!["data"] as Bitmap?
            val bytes = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
            val uri = getImageUri(bitmap)

            val file = File(getPath(uri!!))
            val requestFile =
                RequestBody.create(MediaType.parse("image/jpg"), file)
            imageFile = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        if (requestCode == PICK_IMAGE_GALLERY && data != null) {
            val selectedImage = data.data
            try {
               // binding.ivIcon.setImageURI(selectedImage)

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

    private fun getPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(requireActivity(), uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        Log.d("image path", cursor.getString(column_index))
        return cursor.getString(column_index)
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context!!.contentResolver,
            inImage,
            "ProfilePic",
            null
        )
        return Uri.parse(path)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
       // menu.findItem(R.id.notification).isVisible = false
        //menu.findItem(R.id.cartItem).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }




}
