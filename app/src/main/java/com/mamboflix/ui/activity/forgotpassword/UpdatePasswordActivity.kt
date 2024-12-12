package com.mamboflix.ui.activity.forgotpassword


import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.mamboflix.BaseActivity
import com.mamboflix.R
import com.mamboflix.databinding.ActivityUpdatePasswordBinding
import com.mamboflix.ui.activity.login.LoginActivity
import com.mamboflix.utils.Constants
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.util.regex.Matcher
import java.util.regex.Pattern

class UpdatePasswordActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUpdatePasswordBinding
    private var isVisible2 = false
    private var isVisible1 = false
    private var validation: AwesomeValidation? = null
    private var phoneOtp: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_password)
        setToolBar()
        setListener()
        phoneOtp = intent.extras!!.getString("phone")
        validation()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }


    public override fun onStart() {
        super.onStart()

    }

    private fun validation() {

        validation = AwesomeValidation(ValidationStyle.BASIC)
        validation!!.addValidation(this, R.id.edtPassword, Constants.EMPTY_REGEX, R.string.password)
        validation!!.addValidation(
            this,
            R.id.edt_confirm_password,
            Constants.EMPTY_REGEX,
            R.string.cpassword
        )


    }


    private fun setListener() {
        binding.btnSignUp.setOnClickListener(this)
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

    override fun onClick(view: View?) {
        when (view!!.id) {

            R.id.btnSignUp -> {

                if (binding.edtPassword.text.toString().isEmpty()) {
                    Toast.makeText(this@UpdatePasswordActivity, "Please enter password", Toast.LENGTH_SHORT)
                        .show()
                }else if (binding.edtConfirmPassword.text.toString().isEmpty()) {
                    Toast.makeText(this@UpdatePasswordActivity, "Please enter confirm password", Toast.LENGTH_SHORT)
                        .show()
                }
                else if (!isValidPassword(binding.edtPassword.text.toString())) {
                    Toast.makeText(
                        this@UpdatePasswordActivity,
                        "Password should be strong password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString()) {
                    Toast.makeText(
                        this@UpdatePasswordActivity,
                        "Password and confirm password should be same.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else{
                    callForgotAPI(phoneOtp!!)
                }

             /*   if (validation!!.validate()) {

                    if (binding.edtPassword.text.toString().length < 6) {
                        Toast.makeText(
                            this@UpdatePasswordActivity,
                            "Password please minimum six character",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (binding.edtConfirmPassword.text.toString().length < 6) {
                        Toast.makeText(
                            this@UpdatePasswordActivity,
                            "Password please minimum six character",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else if (binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString()) {
                        Toast.makeText(
                            this@UpdatePasswordActivity,
                            "Password do not mismatch",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    } else {

                        callForgotAPI(phoneOtp!!)

                    }
                }*/
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

    private fun setToolBar() {
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Back"
        binding!!.toolbar.navigationIcon?.setColorFilter(
            this.resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
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

    private fun callForgotAPI(mobile: String) {
        apiService.callForgotAPI(mobile, binding.edtPassword.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::showProgressDialog)
            .doOnCompleted(this::hideProgressDialog)

            .subscribe({ commonResponse ->
                if (commonResponse.status != 0) {
                    Toast.makeText(this, commonResponse.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
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


}