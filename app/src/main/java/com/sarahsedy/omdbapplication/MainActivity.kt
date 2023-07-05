package com.sarahsedy.omdbapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : BaseActivity() {

    private lateinit var btnSearch: Button
    private lateinit var edtSearch: EditText
    private lateinit var recycler: RecyclerView
    private lateinit var recordAdapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch = findViewById(R.id.btnSearch)
        edtSearch = findViewById(R.id.edtTextSearch)

        recycler = findViewById(R.id.rViewRecords)

        recordAdapter = RecordAdapter(cellsList) { _: CellItem, position: Int ->
            currentPosition = position
            val intent = Intent(this, ViewRecord::class.java)
            intent.putExtra("cell_position", currentPosition)
            startActivity(intent)
        }

        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.adapter = recordAdapter

        recordAdapter.notifyDataSetChanged()
    }

    fun searchOnClick(v: View) {
        var userSearch = ""

        //if not null, get text from search input box for api call
        if (edtSearch.text != null) {
            userSearch = edtSearch.text.toString()
        } else {
            toastIt("Please enter a title first.")
        }

        //take userSearch and concatenate link, search, apiKey
        val queryUrl: String = dataRequestLink + userSearch + apiKey
        var cellRecord: CellItem

        val searchRequest = JsonObjectRequest(Request.Method.GET, queryUrl, null, { response ->

                val jsonArray = response.getJSONArray("Search")
                for (i in 0 until 10) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val title = jsonObject.getString("Title")
                    val year = jsonObject.getString("Year")
                    val type = jsonObject.getString("Type")
                    val id = jsonObject.getString("imdbID")
                    cellRecord = CellItem(title, year, id, type)
                    cellsList.add(cellRecord)
                    recordAdapter.notifyDataSetChanged()
                }

            Log.i("OMDb Application", "Response is: $response")
            Log.i("Cell List", "Cell List: $cellsList")
        }, {

            Log.i("OMDb Application", "Error ${it.message}")
        })

        //add the request
        searchRequest.setShouldCache(false)

        RequestSingleton.getInstance(applicationContext)
            .addToRequestQueue(searchRequest)

    }

    fun resetOnClick(v: View) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to clear your search?").setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                cellsList.clear()
                edtSearch.text.clear()
                recordAdapter.notifyDataSetChanged()

            }.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }.create().show()
    }

}