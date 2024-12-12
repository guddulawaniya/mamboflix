package com.mamboflix.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.mamboflix.model.UserModel



import javax.inject.Singleton


@Singleton
class UserPref(context: Context) {
    private val preferences: SharedPreferences
    private val gson: Gson

    var user: UserModel
        get() = gson.fromJson<Any>(preferences.getString("user", null), UserModel::class.java) as UserModel
        set(user) {
            val gson = Gson()
            val loginRes = gson.toJson(user)
            preferences.edit().putString("user", loginRes).apply()
        }



    var isLogin: Boolean
        get() = preferences.getBoolean("isLoginA", false)
        set(login) = preferences.edit().putBoolean("isLoginA", login).apply()

    init {
        preferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE)
        gson = Gson()
    }

    fun clearPref() {
        preferences.edit().clear().apply()
    }


    fun getOTP(): String? {
        return preferences.getString("otp", null)
    }

    fun setOTP(otp: String?) {
        preferences.edit().putString("otp", otp).apply()
    }

    fun get_check_api(): String? {
        return preferences.getString("check_api", null)
    }

    fun set_check_api(check_api: String?) {
        preferences.edit().putString("check_api", check_api).apply()
    }


    fun getToken(): String? {
        return preferences.getString("token", null)
    }

    fun setToken(token: String?) {
        preferences.edit().putString("token", token).apply()
    }

    //for FCM Token
    fun getFcmToken(): String? {
        return preferences.getString("fcmtoken", null)
    }

    fun setFcmToken(token: String?) {
        preferences.edit().putString("fcmtoken", token).apply()
    }


    fun getSubUserName(): String? {
        return preferences.getString("subUser", null)
    }

    fun setSubUserName(subUser: String?) {
        preferences.edit().putString("subUser", subUser).apply()
    }
 fun getset_value(): String? {
        return preferences.getString("set_value", null)
    }

    fun setset_value(set_value: String?) {
        preferences.edit().putString("set_value",set_value).apply()
    }


    fun getSubUserId(): String? {
        return preferences.getString("subUserId", null)
    }

    fun setSubUserId(subUserId: String?) {
        preferences.edit().putString("subUserId", subUserId).apply()
    }

    fun getSubtitles(): Boolean {
        return preferences.getBoolean("isSubtitles", false)
    }

    fun setSubtitles(isSubtitles: Boolean) {
        preferences.edit().putBoolean("isSubtitles", isSubtitles).apply()
    }
    fun getautoplay(): Boolean {
        return preferences.getBoolean("autoplay", false)
    }

    fun setautoplay(autoplay: Boolean) {
        preferences.edit().putBoolean("autoplay", autoplay).apply()
    }

    fun getDownloadWifi(): Boolean {
        return preferences.getBoolean("isDownloadWifi", false)
    }

    fun setDownloadWifi(isDownloadWifi: Boolean) {
        preferences.edit().putBoolean("isDownloadWifi", isDownloadWifi).apply()
    }

    fun getGenere(): Boolean {
        return preferences.getBoolean("isGenere", false)
    }

    fun setGenere(isDownloadWifi: Boolean) {
        preferences.edit().putBoolean("isGenere", isDownloadWifi).apply()
    }

    fun getNotification(): Boolean {
        return preferences.getBoolean("isNotification", true)
    }

    fun setNotification(isNotification: Boolean) {
        preferences.edit().putBoolean("isNotification", isNotification).apply()
    }

    fun getNotificationdot(): Boolean {
        return preferences.getBoolean("isNotification", false)
    }

    fun setNotificationdot(isNotification: Boolean) {
        preferences.edit().putBoolean("isNotificationdot", isNotification).apply()
    }

    fun getPreferredLanguage(): String {
        return preferences.getString("preferredLanguage", "English")!!
    }

    fun setPreferredLanguage(preferredLanguage: String) {
        preferences.edit().putString("preferredLanguage", preferredLanguage).apply()
    }

    fun getLoginType(): String {
        return preferences.getString("loginType", "1")!!
    }

    fun setLoginType(loginType: String) {
        preferences.edit().putString("loginType", loginType).apply()
    }


    fun getAdult(): Boolean {
        return preferences.getBoolean("isAdult", false)
    }

    fun setAdult(isNotification: Boolean) {
        preferences.edit().putBoolean("isAdult", isNotification).apply()
    }



    fun getBannerImg(): String? {
        return preferences.getString("banner", null)
    }

    fun setBannerImg(banner: String?) {
        preferences.edit().putString("banner", banner).apply()
    }
    fun getsubuserImg(): String? {
        return preferences.getString("subuserImg", null)
    }

    fun setsubuserImg(subuserImg: String?) {
        preferences.edit().putString("subuserImg", subuserImg).apply()
    }

    fun getuserImg(): String? {
        return preferences.getString("userImg", null)
    }

    fun setuserImg(userImg: String?) {
        preferences.edit().putString("userImg", userImg).apply()
    }

}