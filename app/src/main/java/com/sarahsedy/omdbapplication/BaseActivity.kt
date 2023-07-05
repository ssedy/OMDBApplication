package com.sarahsedy.omdbapplication

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

var cellsList = ArrayList<CellItem>()
var currentPosition = 0

//Need OMDb link + s= + api key, take user search then append all
//elements together
//https?
const val dataRequestLink = "http://www.omdbapi.com/?s="
//to be used in view record
const val requestIdLink = "http://www.omdbapi.com/?i="
const val apiKey = "&apikey=7db59efc"

open class BaseActivity : AppCompatActivity() {
    open fun toastIt(msg : String?){
        Toast.makeText(
            applicationContext, msg, Toast.LENGTH_SHORT
        ).show()
    }
}