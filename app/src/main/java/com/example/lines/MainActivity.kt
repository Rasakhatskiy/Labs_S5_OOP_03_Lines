package com.example.lines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val pid = Process.myPid()
        val whiteList = "logcat -P '$pid'"
        Runtime.getRuntime().exec(whiteList).waitFor()

        super.onCreate(savedInstanceState)

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)



        setContentView(R.layout.activity_main)
    }
}