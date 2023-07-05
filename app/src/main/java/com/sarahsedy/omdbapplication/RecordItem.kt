package com.sarahsedy.omdbapplication

import android.graphics.Bitmap

data class RecordItem(
    val poster: Bitmap,
    val title : String,
    val rating : String,
    val director : String,
    val plotSummary : String,
    val genre : String,
    val runtime : String,
    val language : String,
    val releaseDate : String,
    val writer : String
)