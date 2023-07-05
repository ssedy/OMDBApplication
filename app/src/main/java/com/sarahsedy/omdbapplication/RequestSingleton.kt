package com.sarahsedy.omdbapplication

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class RequestSingleton constructor (context: Context) {
    companion object {
        @Volatile
        private var instance: RequestSingleton? = null

        //make sure there is only one instance of this class adding to the request queue
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: RequestSingleton(context).also {
                    instance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    //can call on this to add new request to the queue
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}