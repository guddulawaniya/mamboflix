package com.mamboflix.utils

interface Constants {
    companion object {

        //Live server url
       // val BASE_URL = "http://162.214.65.129/~jeevan/mamboflix/api/"
      //  val BASE_URL = "https://mamboflix.tv/api/"
//        val BASE_URL = "https://mamboflix.tv/mamboflixapi/api/"
//        val BASE_URL = "http://182.76.237.233/~max233testmambo/mamboflixapi/api/"

        var BASE_URL =
            "http://103.154.2.116/~max233testmambo/mamboflixapi/api/"

        var list:ArrayList<String> = ArrayList<String>()
        var group_id:String=""
        var backpress:Boolean=false
        var user_name:String=""
        var password:String=""
        val NAME_REGEX = ".{2,}"
        val PASSWORD_REGEX = ".{3,}"
        val EMPTY_REGEX = ".{1,}"
        val IS_NOTIFICATION = "is_notification"
    }

}
