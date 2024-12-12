package com.mamboflix.utils

import android.content.Context
import android.widget.Toast

object ToastObj {
    fun message(context: Context,message: String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}