package com.mamboflix.utils

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface

import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns

import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.mamboflix.R
import java.io.*


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Random
import java.util.TimeZone
import java.util.regex.Matcher
import java.util.regex.Pattern


object CommonUtils {

    private val VALID_NUMBER = Pattern.compile("[0-9]+")


    fun setFragment(fragment: Fragment, removeStack: Boolean, activity: FragmentActivity, mContainer: Int) {
        val fragmentManager = activity.supportFragmentManager
        val ftTransaction = fragmentManager.beginTransaction()
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ftTransaction.replace(mContainer, fragment)
            ftTransaction.addToBackStack(null)
        } else {
            ftTransaction.replace(mContainer, fragment)
            ftTransaction.addToBackStack(null)
        }
        ftTransaction.commit()
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        if (target == null) {
            return false
        } else {

            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(target)
            return matcher.matches()
            // return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }




    /*createCustomDialog() is used to create dialog*/
    fun createCustomDialog(context: Context, layoutResourceId: View): Dialog {
        val dialog = Dialog(context, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(layoutResourceId)
        val layoutParams = dialog.window!!.attributes
        layoutParams.dimAmount = .7f
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window!!.attributes = layoutParams
        return dialog
    }


    /*fun exitDialog(mContext: Context) {
        val dialogLogoutBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(mContext), R.layout.dialog_logout, null, false)
        val exitDialog = CommonUtils.createCustomDialog(mContext, dialogLogoutBinding.getRoot())
        dialogLogoutBinding.tvMessage.setText(R.string.exit_from_app)
        dialogLogoutBinding.tvYes.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            mContext.startActivity(intent)
            System.exit(0)
        })
        dialogLogoutBinding.tvNo.setOnClickListener(View.OnClickListener { exitDialog.dismiss() })
        exitDialog.show()
    }*/


    /* public static void exitDialog(final Context mContext) {
        DialogLogoutBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_logout, null, false);
        final Dialog exitDialog = CommonUtils.createCustomDialog(mContext, dialogLogoutBinding.getRoot());
        dialogLogoutBinding.tvMessage.setText(R.string.exit_from_app);
        dialogLogoutBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                mContext.startActivity(intent);
                System.exit(0);
            }
        });
        dialogLogoutBinding.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
        exitDialog.show();
    }*/


    fun isVin(s: String): Boolean {
        return VALID_NUMBER.matcher(s).matches()
    }


    /**
     * checking internet connection
     */
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    fun isValidPhone(target: String?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.PHONE.matcher(target).matches()
        }
    }

    fun printLog(context: Context?, msg: String) {
        if (context != null) {
            Log.e(context.javaClass.name, msg)
        } else {
            Log.e("DEBUG", msg)
        }
    }

    fun getTimeStamp(str_date: String): String {
        var date: Date? = null
        try {
            date = SimpleDateFormat("MM-dd-yyyy HH:mm").parse(str_date) as Date
        } catch (e1: ParseException) {
            e1.printStackTrace()
        }

        assert(date != null)
        return date!!.time.toString()
    }


    fun getTimeStampDate(str_date: String): String {
        var str_date = str_date
        val format = SimpleDateFormat("dd-MMM-yy HH:mm")
        if (str_date.length < 13) {
            str_date = (java.lang.Long.valueOf(str_date) * 1000).toString() + ""
        }
        var date: Date? = null
        try {
            date = Date(java.lang.Long.valueOf(str_date))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        assert(date != null)
        var timestampValue = ""
        try {
            timestampValue = format.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return timestampValue
    }


    fun showSnack(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }


//    fun commonAlert(mContext: Context, message: String, fragment: Fragment) {
//        val dialogBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(mContext), R.layout.normal_alert, null, false)
//        val commonDialog = CommonUtils.createCustomDialog(mContext, dialogBinding.getRoot())
//        dialogBinding.tvMessage.setText(message)
//        dialogBinding.tvYes.setOnClickListener(View.OnClickListener {
//            commonDialog.dismiss()
//            CommonUtils.setFragment(fragment, true, mContext as FragmentActivity, R.id.frameContainer)
//        })
//
//        commonDialog.show()
//    }


//    fun commonAlertBackStack(mContext: Activity, message: String) {
//        val dialogBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(mContext), R.layout.normal_alert, null, false)
//        val commonDialog = CommonUtils.createCustomDialog(mContext, dialogBinding.getRoot())
//        dialogBinding.tvMessage.setText(message)
//        dialogBinding.tvYes.setOnClickListener(View.OnClickListener {
//            commonDialog.dismiss()
//            (mContext as DashboardFragment).onBackPressed()
//        })
//
//        commonDialog.show()
//    }


    fun call(context: Context, phone: String) {
        try {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phone")
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            context.startActivity(callIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun animate(): Animation {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 80 //You can manage the time of the blink with this parameter
        anim.startOffset = 30
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        return anim
    }

    /* public static void alertMessage(final Context mContext, String message, final Fragment fragment) {
        DialogLogoutBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_logout, null, false);
        final Dialog exitDialog = CommonUtils.createCustomDialog(mContext, dialogLogoutBinding.getRoot());
        dialogLogoutBinding.tvMessage.setText(message);
        dialogLogoutBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               exitDialog.dismiss();
            }
        });
        dialogLogoutBinding.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                CommonUtils.setFragment(fragment, true, (FragmentActivity) mContext, R.id.flContainerHome);

            }
        });
        exitDialog.show();
    }*/


    /**
     * Here is the key method to apply the animation
     */
    fun setAnimation(viewToAnimate: View, context: Context) {
        /* Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);*/

        val anim = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = Random().nextInt(501).toLong()//to make duration random number between [0,501)
        viewToAnimate.startAnimation(anim)

    }

    /* public static void alertMessageNotRegister(final Context mContext, String message) {
        NormalAlertBinding normalAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.normal_alert, null, false);
        final Dialog exitDialog = CommonUtils.createCustomDialog(mContext, normalAlertBinding.getRoot());
        normalAlertBinding.tvMessage.setText(message);
        normalAlertBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                Intent intent=new Intent(mContext,RegistrationActivity.class);
                mContext.startActivity(intent);
            }
        });
        exitDialog.show();
    }*/

    /* public static void logoutDialog(final Context mContext) {
        DialogLogoutBinding dialogLogoutBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_logout, null, false);
        final Dialog exitDialog = CommonUtils.createCustomDialog(mContext, dialogLogoutBinding.getRoot());
        dialogLogoutBinding.tvMessage.setText(R.string.logout_from_app);
        dialogLogoutBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
                SharedPref.removePreference(mContext);
                SharedPref.savePreferenceBoolean(mContext,AppConstant.NOT_FIRST_TIME,true);
                Intent intent = new Intent(mContext,RegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);

            }
        });
        dialogLogoutBinding.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
        exitDialog.show();
    }*/


    fun datePickerDialog(context: Context, textView: TextView) {
        val c = Calendar.getInstance(TimeZone.getDefault())
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(context, R.style.MyTimePickerDialogTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear + 1)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.time
            textView.text = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
        }, mYear, mMonth, mDay)
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.setCanceledOnTouchOutside(true)
        dpd.show()
    }


    fun setTime(mContext: Context, textView: TextView) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(mContext, R.style.MyTimePickerDialogTheme, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            var hour = selectedHour.toString() + ""
            var min = selectedMinute.toString() + ""
            if (selectedHour < 10) {
                hour = "0$selectedHour"
            }
            if (selectedMinute < 10) {
                min = "0$selectedMinute"
            }
            textView.text = "$hour:$min"
        }, hour, 0, false)//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()

    }


    const val PER_CAMERA = Manifest.permission.CAMERA
    const val PER_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val PER_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    fun checkPermission(
        context: Context?,
        permission: String?
    ): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    //define functions for each permission
    fun isCameraGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_CAMERA
        )
    }

    fun isWriteStorageGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_WRITE_STORAGE
        )
    }

    fun isReadStorageGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_READ_STORAGE
        )
    }

    fun requestPermissions(
        activity: Activity?,
        permissionId: Int,
        vararg permissions: String?
    ) {
        ActivityCompat.requestPermissions(activity!!, permissions, permissionId)
    }


    fun createImageFile(mContext: Context): File {
        val imageFileName = "MamboFlix" + "_" + System.currentTimeMillis()
        val storageDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pictures"
            )
        } else {
            File(Environment.getExternalStorageDirectory(), "MamboFlix")
        }

        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        var image: File
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            val newImageFileName = "Dally Dollars" + "-" + System.currentTimeMillis() + ".jpg"
            image = File(storageDir, newImageFileName)
            try {
                image.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return image
    }


    fun changeImageOrientation(photoPath: String?, bitmap: Bitmap): Bitmap? {
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(photoPath!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var orientation = 0
        if (ei != null) {
            orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
        }
        val rotatedBitmap: Bitmap
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                bitmap,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                bitmap,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                bitmap,
                270f
            )
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }



    fun getResizedBitmap(
        bitmapImage: Bitmap?,
        bitmapWidth: Int,
        bitmapHeight: Int
    ): Bitmap? {
        return Bitmap.createScaledBitmap(bitmapImage!!, bitmapWidth, bitmapHeight, true)
    }

    fun createFileFromBitMap(bitmap: Bitmap): File? {
        val file: File
        val imageFileName =
            "MamboFlix App" + "-" + System.currentTimeMillis() + ".jpg"
        val myDirectory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pictures"
            )
        } else {
            File(Environment.getExternalStorageDirectory(), "MamboFlix App")
        }
        if (!myDirectory.exists()) {
            myDirectory.mkdir()
        }
        file = File(myDirectory, imageFileName)
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        //write the bytes in file
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file
    }

    //    fun getRealPathFromUri(context: Context, uri: Uri?): File? {
//        try {
//            val inputStream = context.contentResolver.openInputStream(uri!!)
//            val fileName: String = getFileName(context, uri)!!
//            val splitName: Array<String> = splitFileName(fileName)!!
//            var tempFile = File.createTempFile(splitName[0], splitName[1])
//            tempFile = rename(tempFile, fileName)
//            tempFile.deleteOnExit()
//            val out = FileOutputStream(tempFile)
//            if (inputStream != null) {
//                copy(inputStream, out)
//                inputStream.close()
//            }
//            out.close()
//            return tempFile
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//        return null
//    }
    fun getRealPathFromUri(context: Context, uri: Uri?): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val fileName: String = getFileName(context, uri)!!
            val splitName: Array<String> = splitFileName(fileName)!!
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            val out = FileOutputStream(tempFile)
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out.close()
            return tempFile
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    private fun copy(input: InputStream, output: OutputStream) {
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (-1 != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
        }
    }

    private fun rename(file: File, newName: String): File? {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme != null && uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            if (result != null) {
                val cut = result.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
        }
        return result
    }

    private fun splitFileName(fileName: String): Array<String>? {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    fun compressImage(filePath: String?): String? {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

// by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
// you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

// max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 1024.0f
        val maxWidth = 1024.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

// width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

// setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

// inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

// this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
// load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

// check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val out: FileOutputStream
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

// write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight;
        val width = options.outWidth;
        var inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio
            } else {
                inSampleSize = widthRatio
            }

        }
        var totalPixels = width * height;
        var totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private fun getFilename(): String? {
        val file = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
// val file = File(Environment.getExternalStorageDirectory().path, ".spongy/Images")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
    }





}