package com.example.lines

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Main game view.
 * No game logic here.
 * Drawing and controls only.
 */
class GameView(
    context: Context,
    attributes: AttributeSet
) : SurfaceView(context, attributes),
    SurfaceHolder.Callback {


    private val thread: GameThread

    private var ball: Ball? = null
    private var field: Field =
        Field(BitmapFactory.decodeResource(resources, R.drawable.battlefield))
    private val cellSizePx = field.cellSize
    private var logic: GameLogic = GameLogic(cellSizePx)

    private var ballOptionsList: List<BallSlot> = listOf()
    private var ballList: MutableList<Ball> = mutableListOf<Ball>()

    private var touchedX = 0
    private var touchedY = 0
    private var touched = false

    private var selectedBall: Ball? = null

    init {

        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)
        logic = GameLogic(cellSizePx)
        logic.genRandBall(5)
        initBallsSprites()
        ballList = logic.getBallsSprites()
    }

    /**
     * Inits ball sprites
     */
    // todo different colors
    fun initBallsSprites() {
        for (i in 0 until logic.maxHorCells) {
            for (j in 0 until logic.maxVerCells) {
                logic.ballMap[i][j].ball = Ball(
                    BitmapFactory.decodeResource(resources, R.drawable.ball),
                    logic.ballMap[i][j].color,
                    i,
                    j,
                    cellSizePx
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // when ever there is a touch on the screen,
        // we can get the position of touch
        // which we may use it for tracking some of the game objects
        touchedX = event.x.toInt()
        touchedY = event.y.toInt()
        touched = false

        val action = event.action
        when (action) {
//            MotionEvent.ACTION_DOWN -> touched = true
//            MotionEvent.ACTION_MOVE -> touched = true
            MotionEvent.ACTION_UP -> touched = true
//            MotionEvent.ACTION_CANCEL -> touched = false
//            MotionEvent.ACTION_OUTSIDE -> touched = false
        }

        return true
    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update() {
        logic.update(touched, touchedX, touchedY)

        for (ball in ballList) {
            ball.update()
        }
        touched = false
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        field.draw(canvas)

        for (ball in ballList) {
            ball.draw(canvas)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
//        ball = Ball(BitmapFactory.decodeResource(resources, R.drawable.ball))
        //field = Field(BitmapFactory.decodeResource(resources, R.drawable.battlefield))


        //To change body of created functions use File | Settings | File Templates.
        // start the game thread
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        //To change body of created functions use File | Settings | File Templates.
        //TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        //To change body of created functions use File | Settings | File Templates.
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

}