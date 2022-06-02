package com.example.catapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public val TAG = "MainActivity"

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCurrentData()
        val layout_generate_new_fact = findViewById<RelativeLayout>(R.id.layout_generate_new_fact)
        layout_generate_new_fact.setOnClickListener {
            getCurrentData()
        }
    }
    object RetrofitHelper {

        val baseUrl = "https://cat-fact.herokuapp.com"

        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                // we need to add converter factory to
                // convert JSON object to Java object
                .build()
        }
    }
    private fun getCurrentData() {
        val tv_textView: TextView = findViewById(R.id.tv_textView)
        val tv_timeStamp: TextView = findViewById(R.id.tv_timeStamp)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)


        tv_textView.visibility = View.GONE
        tv_timeStamp.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        val api = MainActivity2.RetrofitHelper.getInstance().create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = api.getCatFacts()
                if (result != null) {

                    val data = result.body()!!
                    Log.d(TAG, data.toString())

                    withContext(Dispatchers.Main) {
                        tv_textView.visibility = View.VISIBLE
                        tv_timeStamp.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        tv_timeStamp.text = data.createdAt
                        tv_textView.text = data.text

                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        applicationContext,
                        "BRUHH:////",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }
}