package com.example.lines


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas


class Field(var image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0

    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    val aspect: Double = image.width.toDouble() / image.height.toDouble()
    val cellSize: Int = screenWidth / 8


    init {
        image = Bitmap.createScaledBitmap(image, screenWidth, (screenWidth / aspect).toInt(), false);

        w = image.width
        h = image.height

        x = 0//screenWidth / 2
        y = 0//screenHeight / 2

    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

}