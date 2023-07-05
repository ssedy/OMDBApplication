package com.sarahsedy.omdbapplication

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.createBitmap
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import java.net.URI

class ViewRecord : BaseActivity() {

    private lateinit var btnToMain : Button
    private lateinit var txtTitle : TextView
    private lateinit var txtRated : TextView
    private lateinit var txtDirector : TextView
    private lateinit var txtPlot : TextView
    private lateinit var txtGenre : TextView
    private lateinit var txtRuntime : TextView
    private lateinit var txtReleased : TextView
    private lateinit var txtLanguage : TextView
    private lateinit var txtImdbRating : TextView
    private lateinit var txtAwards : TextView
    private lateinit var txtLink : TextView


    private lateinit var currentCell : CellItem
    private lateinit var cellID : String

    private var posterLink = ""
    private var poster = createBitmap(1000,1000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_record)

        btnToMain = findViewById(R.id.btnMain)
        txtTitle = findViewById(R.id.txtTitle)
        txtRated = findViewById(R.id.txtRated)
        txtDirector = findViewById(R.id.txtDirector)
        txtPlot = findViewById(R.id.txtPlot)
        txtGenre = findViewById(R.id.txtGenre)
        txtRuntime = findViewById(R.id.txtRuntime)
        txtReleased = findViewById(R.id.txtRelease)
        txtLanguage = findViewById(R.id.txtLang)
        txtImdbRating = findViewById(R.id.txtIMDbRate)
        txtAwards = findViewById(R.id.txtAwards)
        txtLink = findViewById(R.id.txtLink)

        val extras = intent.extras

        if (extras != null){
            currentPosition = extras.getInt("cell_position")
        }

        currentCell = cellsList[currentPosition]

        //set up url for volley request by getting the imdb id from the current cell
        cellID = currentCell.imdbID

        val viewObjectURL = requestIdLink + cellID + apiKey

        val requestViewRecord = JsonObjectRequest(Request.Method.GET, viewObjectURL, null, { response ->

            for (i in 0 until response.length()) {

                txtTitle.text = getString(R.string.txt_title,response.getString("Title"))
                txtRated.text = getString(R.string.txt_rated,response.getString("Rated"))
                txtDirector.text = getString(R.string.txt_director,response.getString("Director"))
                txtPlot.text = getString(R.string.txt_plot, response.getString("Plot"))
                txtGenre.text = getString(R.string.txt_genre,response.getString("Genre"))
                txtRuntime.text = getString(R.string.txt_runtime,response.getString("Runtime"))
                txtReleased.text = getString(R.string.txt_release,response.getString("Released"))
                txtLanguage.text = getString(R.string.txt_language,response.getString("Language"))
                txtImdbRating.text = getString(R.string.txt_imdb_rate,response.getString("imdbRating"))
                txtAwards.text = getString(R.string.txt_awards,response.getString("Awards"))
                posterLink = response.getString("Poster")

                txtLink.text = posterLink

                Log.i("Poster Link", posterLink)

            }
            Log.i("OMDb API", "Response is: $response")
        }, {

            Log.i("OMDb API", "Error ${it.message}")
        })

        //add the request
        requestViewRecord.setShouldCache(false)

        RequestSingleton.getInstance(applicationContext)
            .addToRequestQueue(requestViewRecord)

        txtLink.setOnClickListener{
            showPosterDialog()
        }

    }

    private fun showPosterDialog(){
        val posterDialog = Dialog(this)

        posterDialog.setContentView(R.layout.poster_view)

        onClick(posterDialog)

        val textView : TextView = posterDialog.findViewById(R.id.txtDialog)
        textView.text = getString(R.string.txt_dialog)
        val imageView : ImageView = posterDialog.findViewById(R.id.posterImgView)

        val uriBuilder = URI(posterLink)
        val uri = uriBuilder.toString()

        uri.replace("\\","")

        val posterRequest = ImageRequest(uri, { response ->

            poster = response
            imageView.setImageBitmap(poster)
            Log.i("OMDb API", uri)
            Log.i("OMDb API", "Poster requested")
        }, 0,
            0,
            ImageView.ScaleType.FIT_CENTER,
            Bitmap.Config.ARGB_8888,
            {
                Log.i("OMDb API", "Error ${it.message}")
            })

        posterRequest.setShouldCache(false)
        RequestSingleton.getInstance(applicationContext).addToRequestQueue(posterRequest)

        imageView.setOnClickListener{posterDialog.dismiss()}
        textView.setOnClickListener{posterDialog.dismiss()}

        posterDialog.show()
    }

    private fun onClick(dialog: DialogInterface) {
        dialog.dismiss()
    }

    fun returnToMain(v : View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}