package com.example.lines


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

/**
 * Ball sprite class.
 * It is not logical object.
 * Drawing stuff only
 */
class Ball(
    var image: Bitmap,
    val ballColor: BallColor,
    var spritePosX: Int,
    var spritePosY: Int,
    private val cellSize: Int
) {
    // sprite options
    private var spriteWidth: Int = 0
    private var spriteHeight: Int = 0
    private val borderOffset = 4

    // selection effect
    public var isSelected = false
    private var toIncreaseAlpha = true
    private var alpha = 255
    private val blinkingSpeed = 10
    private val maxAlphaValue = 255
    private val minAlphaValue = 50
    private val alphaPaint = Paint()


    init {
        // scale ball
        image = Bitmap.createScaledBitmap(
            image,
            cellSize - borderOffset,
            cellSize - borderOffset,
            false
        );


        spriteWidth = image.width
        spriteHeight = image.height

        spritePosX *= cellSize
        spritePosY *= cellSize
    }

    fun teleport(toX: Int, toY: Int) {
        spritePosX = toX * cellSize
        spritePosY = toY * cellSize
    }

    /**
     * Draws the object on to the canvas.
     */
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, spritePosX.toFloat(), spritePosY.toFloat(), alphaPaint)
    }

    /**
     * update properties for the game object
     */
    fun update() {
        if (isSelected) {
            if (toIncreaseAlpha) {
                alpha += blinkingSpeed

                if (alpha >= maxAlphaValue) {
                    alpha = maxAlphaValue
                    toIncreaseAlpha = false
                }
            } else {
                alpha -= blinkingSpeed
                if (alpha <= minAlphaValue) {
                    alpha = minAlphaValue
                    toIncreaseAlpha = true
                }
            }
        } else {
            alpha = maxAlphaValue
        }
        alphaPaint.alpha = alpha
        // val randomNum = ThreadLocalRandom.current().nextInt(1, 5)
    }

    fun detectSelection(touchX: Int, touchY: Int): Boolean {
        return (touchX > spritePosX &&
                touchX < spritePosX + spriteWidth &&
                touchY > spritePosY &&
                touchY < spritePosY + spriteHeight)
    }




}