package com.lbaron.flyingweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        u.l(this, "Checking Internet Connected")
        if(u.isNetworkAvailable(this)){
            u.l(this, "Connected to Internet")
        } else {
            u.l(this, "No Internet Connection")
        }
    }
}
