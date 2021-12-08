package com.example.lines

import kotlin.random.Random


/**
 * If there is no ball, color is None and sprite is null
 */
class BallSlot(
    // Logical color
    public var color: BallColor = BallColor.Red
) {
    // Sprite
    public var ball: Ball? = null
}

class SlotCoord(
    public val slot: BallSlot,
    public val x: Int,
    public val y: Int
)

/**
 * Main logic of the game here
 */
class GameLogic(
    val cellSize: Int
) {
    // field size 8x14
    val maxHorCells: Int = 8
    val maxVerCells: Int = 14
    val numberBallsToGen = 3

    var selectedSlot: BallSlot? = null
    var selectedX: Int = 0
    var selectedY: Int = 0


    var toRegenSprites = false

    // init game field with empty cells
    var ballMap: Array<Array<BallSlot>> = Array(maxHorCells) {
        Array(maxVerCells) {
            BallSlot(BallColor.None)
        }
    }

    // generate 1 ball in random position, without sprites
    fun genRandBallSingle(x: Int, y: Int) {
        val colorInt = (0..4).random()
        if (ballMap[x][y].color == BallColor.None) {
            ballMap[x][y].color = BallColor.fromInt(colorInt)
        }
    }

    // generate n balls in random positions, without sprites
    fun genRandBall(_n: Int) {
        var n = _n
        while (n > 0) {
            val w = Random.nextInt(0, maxHorCells)
            val h = Random.nextInt(0, maxVerCells)
            if (ballMap[w][h].color == BallColor.None) {
                genRandBallSingle(w, h)
                n--
            }
        }

    }

    // check left free space on field
    fun checkFreeSpace(n: Int): Boolean {
        var free: Int = 0
        for (row in ballMap) {
            for (slot in row) {
                if (slot.color == BallColor.None) {
                    free++
                }
                if (free >= n) {
                    return true
                }
            }
        }
        return false
    }

    fun canGoTo(fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        val bfs = BFS(ballMap)
        return bfs.checkPath(fromX, fromY, toX, toY)
    }

    /**
     * Returns sprites of all existing balls
     */
    fun getBallsSprites(): MutableList<Ball> {
        val result = mutableListOf<Ball>()
        for (i in 0 until maxHorCells) {
            for (j in 0 until maxVerCells) {
                if (ballMap[i][j].color != BallColor.None &&
                    ballMap[i][j].ball != null
                ) {
                    result.add(ballMap[i][j].ball!!)
                }
            }
        }
        return result
    }

    /**
     * Returns cell on the game field with color and sprite
     */
    fun getFieldSlot(touchX: Int, touchY: Int): SlotCoord? {
        val x = touchX / cellSize
        val y = touchY / cellSize

        // field border checking
        if (x < 0 ||
            x >= maxHorCells ||
            y < 0 ||
            y >= maxVerCells
        ) {
            println("error x:${x}, y:${y}")
            return null
        }
        return SlotCoord(ballMap[x][y], x, y)
    }


    fun deleteBall(X: Int, Y: Int) {
        if (X < 0 ||
            X >= maxHorCells ||
            X < 0 ||
            X >= maxVerCells
        ) {
            println("error x:${X}, y:${Y}")
        } else {
            ballMap[X][Y].color = BallColor.None
            ballMap[X][Y].ball = null
        }
    }


    private fun checkDeleteVertical():Boolean {

        for (i in 0 until maxHorCells) {
            for (j in 0 until maxVerCells-4) {
                if (ballMap[i][j].color != BallColor.None) {
                    if (ballMap[i][j].color == ballMap[i][j + 4].color) {
                        if (ballMap[i][j].color == ballMap[i][j + 3].color) {
                            if (ballMap[i][j].color == ballMap[i][j + 2].color) {
                                if (ballMap[i][j].color == ballMap[i][j + 1].color) {
                                    deleteBall(i, j)
                                    deleteBall(i, j + 1)
                                    deleteBall(i, j + 2)
                                    deleteBall(i, j + 3)
                                    deleteBall(i, j + 4)
                                    return false
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }




    fun checkDeleteHorizontal():Boolean {
        for (j in 0 until maxVerCells) {
            for (i in 0 until maxHorCells-4) {
                if (ballMap[i][j].color != BallColor.None) {
                    if (ballMap[i][j].color == ballMap[i + 4][j].color) {
                        if (ballMap[i][j].color == ballMap[i + 3][j].color) {
                            if (ballMap[i][j].color == ballMap[i + 2][j].color) {
                                if (ballMap[i][j].color == ballMap[i + 1][j].color) {
                                    deleteBall(i + 4, j)
                                    deleteBall(i + 3, j)
                                    deleteBall(i + 2, j)
                                    deleteBall(i + 1, j)
                                    deleteBall(i, j)
                                    return false

                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }


    fun update(
        touched: Boolean,
        touchX: Int,
        touchY: Int,
    ) {


        // Someone touched screen.
        if (touched) {
            val slotCoord = getFieldSlot(touchX, touchY)



            // Was field touched?
            if (slotCoord != null) {

                var slot = slotCoord.slot
                var toX = slotCoord.x
                var toY = slotCoord.y


                // If selected empty field &
                if (slot.color == BallColor.None) {

                    // If ball is already selected
                    // Move ball
                    // todo pathfind

                    if (selectedSlot != null &&
                        canGoTo(selectedX, selectedY, toX, toY)
                    ) {
                        slot.color = selectedSlot!!.color
                        slot.ball = selectedSlot!!.ball
                        slot.ball!!.teleport(touchX / cellSize, touchY / cellSize)
                        slot.ball!!.isSelected = false
                        selectedSlot!!.color = BallColor.None
                        selectedSlot!!.ball = null
                        selectedSlot = null


                        if (checkFreeSpace(numberBallsToGen)) {
                            if (checkDeleteVertical() == true||checkDeleteHorizontal()||true){
                                genRandBall(numberBallsToGen)
                            }

                            toRegenSprites = true

                            checkDeleteVertical()
                            checkDeleteHorizontal()
                        }

                    }
                } else { // Ball was selected
                    if (selectedSlot != null) {
                        selectedSlot!!.ball!!.isSelected = false
                    }
                    slot.ball!!.isSelected = true
                    selectedSlot = slot
                    selectedX = toX
                    selectedY = toY
                }

            }
        }

    }
}