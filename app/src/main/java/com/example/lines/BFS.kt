package com.example.lines

import java.io.IOException
import java.util.*

internal class Point(var x: Int, var y: Int)

internal class QueueNode(
    var point: Point,   // The coordinates of a cell
    var distance: Int   // Cell's distance of from the source
)

class BFS(
    private val ballMap: Array<Array<BallSlot>>
) {

    var W: Int = 0
    var H: Int = 0

    // These arrays are used to get row and column
    // numbers of 4 neighbours of a given cell
    var rowDirNum = intArrayOf(-1, 0, 0, 1)
    var colDirNum = intArrayOf(0, -1, 1, 0)

    init {
        if (ballMap.isEmpty() ||
            ballMap[0].isEmpty()
        ) {
            throw IllegalArgumentException("BRUH")
        }

        W = ballMap.size
        H = ballMap[0].size
    }

    var huy = 0;

    private fun createVisitedMap(x: Int, y: Int): Array<Array<Boolean>> {
        return Array(x) {
            Array(y) {
                false
            }
        }
    }

    private fun isValid(x: Int, y: Int): Boolean {
        return (x in 0 until W) && (y in 0 until H)
    }

    private fun printVisited(visited: Array<Array<Boolean>>, dst: Point) {
        var cnt = 0
        var str: String = "" + ++cnt + "\n"
        for (i in W - 1 downTo 0) {
            for (j in 0 until H) {

                if (i == dst.x && j == dst.y) {
                    str += "X "
                    continue
                }

                if (ballMap[i][j].color != BallColor.None) {
                    str += "# "
                    continue
                }

                if (!visited[i][j]) {
                    str += ". "
                } else {
                    str += "* "
                }

            }
            str += "" + ++cnt + "\n"
        }
        str += "" + ++cnt + "\n"
        str += "" + ++cnt + "\n"
        println(str)
    }


    fun checkPath(
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        val visitedMatrix = createVisitedMap(W, H)

        val src = Point(fromX, fromY)
        val dst = Point(toX, toY)

        visitedMatrix[fromX][fromY] = true

        val queue: Queue<QueueNode> = LinkedList()
        queue.add(QueueNode(src, 0))


        // Do a BFS starting from source cell
        while (!queue.isEmpty()) {

            printVisited(visitedMatrix, dst)

            val curr = queue.peek()
            val point = curr!!.point

            // If we have reached the destination cell, we are done
            if (point.x == dst.x &&
                point.y == dst.y
            ) {
                println(curr.distance)
                return true
            }

            // Otherwise dequeue the front cell
            // in the queue and enqueue
            // its adjacent cells
            queue.remove()

            for (i in 0 until 4) {
                val row = point.x + rowDirNum[i]
                val col = point.y + colDirNum[i]

                // if adjacent cell is valid, has path
                // and not visited yet, enqueue it.
                if (isValid(row, col) &&
                    ballMap[row][col].color == BallColor.None &&
                    !visitedMatrix[row][col]
                ) {
                    // mark cell as visited and enqueue it
                    visitedMatrix[row][col] = true
                    queue.add(QueueNode(Point(row, col), curr.distance + 1))
                }
            }
        }
        return false
    }
}