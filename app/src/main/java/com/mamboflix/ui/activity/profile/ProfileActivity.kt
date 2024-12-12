package com.mamboflix.ui.activity.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityProfileBinding
import com.mamboflix.ui.activity.forgotpassword.ForgotPasswordActivity
import com.mamboflix.ui.activity.purchsedhistory.PurchasedHistory
import com.mamboflix.utils.Constants
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.regex.Matcher
import java.util.regex.Pattern

class ProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private var datePickerDialog: DatePickerDialog? = null
    private var validation: AwesomeValidation? = null
    var year1: String? = null
    var month1: String? = null
    var day1: String? = null
    private var isVisible2 = false
    private var isVisible1 = false
    var flag : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        getUserProfileDetail()
        setToolBar()
        initializeValidation()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding.footer.visibility = View.GONE
        binding.changepassword.setOnClickListener {
            if (flag == false){
                binding.footer.visibility = View.VISIBLE
                binding.btnSave.visibility = View.VISIBLE
                val animation: Animation = RotateAnimation(
                    0.0f,
                    180.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                animation.interpolator = DecelerateInterpolator()
                animation.repeatCount = 0
                animation.fillAfter = true
                animation.duration = 300
                binding.arrowDown.startAnimation(animation)
                flag = true
            } else {
                binding.footer.visibility = View.GONE
                binding.btnSave.visibility = View.GONE
                val animation: Animation = RotateAnimation(180.0f,
                    0.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                animation.interpolator = DecelerateInterpolator()
                animation.repeatCount = 0
                animation.fillAfter = true
                animation.duration = 300
                binding.arrowDown.startAnimation(animation)
                flag = false
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        setValue()
    }

    override fun onResume() {
        super.onResume()
        setValue()
    }

    private fun setValue() {
        binding.tvName.text = userPref.user.name
        binding.tvMobile.text = userPref.user.mobile
        binding.tvEmail.text = userPref.user.email
        Glide.with(this).load(userPref.getsubuserImg())
            .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
            .apply(RequestOptions.errorOf(R.drawable.user_profile))
            .into(binding.ivUser)
//        if (!userPref.user.profile_image.isNullOrBlank()) {
//            Glide.with(this).load(Uri.parse(userPref.user.profile_image))
//                .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
//                .apply(RequestOptions.errorOf(R.drawable.user_profile))
//                .into(binding.ivUser)
//        }
    }
    private fun initializeValidation() {
        validation = AwesomeValidation(ValidationStyle.BASIC)
        validation!!.addValidation(
            binding.edtPassword,
            Constants.EMPTY_REGEX,
            "Please enter the valid Old Password."
        )
        validation!!.addValidation(
            binding.edtConfirmPassword,
            Constants.EMPTY_REGEX,
            "Please enter the New Password."
        )
        binding.btnSave.setOnClickListener(this)
        binding.tvEdit.setOnClickListener(this)
        binding.tvForgot.setOnClickListener(this)
        binding.history.setOnClickListener(this)
        setListener()
    }
    private fun setListener() {
        binding.lytVisiblePass.setOnClickListener {
            if (isVisible1) {
                binding.edtPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisiblePass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible
                        )
                    )
                    binding.edtPassword.setSelection(binding.edtPassword.text.length)
                }
                isVisible1 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.edtPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisiblePass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible_hover
                        )
                    )
                    binding.edtPassword.setSelection(binding.edtPassword.text.length)
                }
                isVisible1 = true
            }
        }
        binding.lytVisibleConfirmPass.setOnClickListener {
            if (isVisible2) {
                binding.edtConfirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleConfirmPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible
                        )
                    )
                    binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text.length)
                }
                isVisible2 = false
            } else { //Toast.makeText(this,"show",Toast.LENGTH_SHORT).show();
                binding.edtConfirmPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.ivVisibleConfirmPass.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.not_visible_hover
                        )
                    )
                    binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text.length)
                }
                isVisible2 = true
            }
        }
    }
    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Back"
        binding.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
    }
    override fun onClick(view: View?) {
        //tvDate
        when (view!!.id) {
            R.id.btnSave -> {
//                if (validation!!.validate()) {
//                    if (edtPassword.text.toString() == edt_confirm_password.text.toString()) {
//                        Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show()
//                    } else {
//                        callUpdatePasswordApi()
//                    }
//                }

                if (binding.edtPassword.text.toString().isEmpty()) {
                    Toast.makeText(this@ProfileActivity, "Please enter password", Toast.LENGTH_SHORT)
                        .show()
                }else if (binding.edtConfirmPassword.text.toString().isEmpty()) {
                    Toast.makeText(this@ProfileActivity, "Please enter confirm password", Toast.LENGTH_SHORT)
                        .show()
                }
                else if (!isValidPassword(binding.edtPassword.text.toString())) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Password should be strong password",
                        Toast.LENGTH_SHORT
                    ).show()
                }  else{
                    callUpdatePasswordApi()
                }


                /*if(validation!!.validate()) {

                    if (binding.edtPassword.text.toString() == binding.edtConfirmPassword.text.toString()){
                        callSignUpAPI()
                    }else{
                        utils.simpleAlert(this,"Error", "Password do not match" )
                    }
                }*/
            }
            R.id.history -> {
                startActivity(Intent(this, PurchasedHistory::class.java))
            }
            R.id.tvEdit -> {
                //EditProfileActivity
                startActivity(Intent(this, EditProfileActivity::class.java))
            }
            R.id.tvForgot -> {
                startActivity(
                    Intent(this, ForgotPasswordActivity::class.java)
                        .putExtra("value", 1)
                )
            }
        }
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN: String =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{6,}"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
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
    //callUpdatePasswordApi
    private fun callUpdatePasswordApi() {
        /* val jsonObject = JsonObject()
         jsonObject.addProperty("device_token", "token123"))*/
        apiService.callUpdatePasswordApi(
            "Bearer " + userPref.getToken(),
            binding.edtPassword.text.toString(),
            binding.edtConfirmPassword.text.toString()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    //binding.edtPassword.setText("")
                    //binding.edtConfirmPassword.setText("")
                    utils.simpleAlert1(
                        this,
                        "Successfully",
                        resources.getString(R.string.change_password1), 0
                    )
                    userPref.clearPref()
                    //Toast.makeText(this,commonResponse.message,Toast.LENGTH_SHORT).show()

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
                        throwable.message.toString()
                    )
                }
            })
    }
    private fun getUserProfileDetail() {
        apiService.userData("Bearer " + userPref.getToken(), userPref.getFcmToken().toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ commonResponse ->
                if (commonResponse.status != 0 && commonResponse.user_data != null) {
                    Log.d("TAG", "getUserProfileDetail: "+commonResponse.profile)
                    Glide.with(this).load(commonResponse.profile)
                        .apply(RequestOptions.placeholderOf(R.drawable.user_profile))
                        .apply(RequestOptions.errorOf(R.drawable.user_profile))
                        .into(binding.ivUser)
                }

            }, { throwable ->
                //hideProgressDialog()
                if (throwable is ConnectException) {

                } else {
                }
            })
    }
}
