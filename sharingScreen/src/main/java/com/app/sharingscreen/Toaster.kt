package com.app.sharingscreen

import android.content.Context
import android.widget.Toast

class Toaster {
    fun showToast(context: Context,msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}