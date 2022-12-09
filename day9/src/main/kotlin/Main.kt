import java.io.File
import java.nio.file.Paths
import kotlin.math.abs

fun main(args: Array<String>) {
    assert(solve(2, "testInput.txt") == 13)
    assert(solve(10, "testInput2.txt") == 36)

    solve(2, "Input.txt")
    solve(10, "Input.txt")
}

fun solve(n: Int, fileName: String): Int {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + fileName
    val ropeList: List<MovablePoint> = List(n) { MovablePoint() }
    File(filePath).forEachLine { line ->
        val directionSteps = line.split(" ")
        val direction = directionSteps[0]
        val steps = directionSteps[1].toInt()
        for (i in 0 until steps) {
            when (direction) {
                "R" -> ropeList.first().moveRight()
                "L" -> ropeList.first().moveLeft()
                "D" -> ropeList.first().moveDown()
                "U" -> ropeList.first().moveUp()
                else -> throw Exception("Unknown direction")
            }
            for (i in 1 until ropeList.size) {
                ropeList[i].follow(ropeList[i - 1])
            }
        }
    }
    println("Tail visited ${ropeList.last().getNumberOfVisitedPlaces()} places")
    return ropeList.last().getNumberOfVisitedPlaces()
}

class MovablePoint {
    var coordinate: Coordinate = Coordinate(0, 0)
    private val visited: MutableSet<Coordinate> = mutableSetOf(coordinate)

    private fun isAdjacentTo(other: MovablePoint): Boolean {
        val xDiff = abs(coordinate.x - other.coordinate.x)
        val yDiff = abs(coordinate.y - other.coordinate.y)

        if (xDiff > 1 || yDiff > 1) {
            return false
        }
        return true
    }

    fun printVisited() {
        for (coord in visited) {
            println("Visited (${coord.x}, ${coord.y}})")
        }
    }

    fun getNumberOfVisitedPlaces(): Int {
        return visited.size
    }

    fun follow(other: MovablePoint) {
        while (!isAdjacentTo(other)) {
            val moveRight = coordinate.x < other.coordinate.x
            val moveLeft = coordinate.x > other.coordinate.x
            val moveUp = coordinate.y < other.coordinate.y
            val moveDown = coordinate.y > other.coordinate.y
            if (moveUp) {
                if (moveRight) {
                    moveUpRight()
                } else if (moveLeft) {
                    moveUpLeft()
                } else {
                    moveUp()
                }
            } else if (moveDown) {
                if (moveRight) {
                    moveDownRight()
                } else if (moveLeft) {
                    moveDownLeft()
                } else {
                    moveDown()
                }
            } else if (moveLeft) {
                moveLeft()
            } else {
                moveRight()
            }
        }
    }

    fun moveUp() {
        moveTo(Coordinate(coordinate.x, coordinate.y + 1))
    }

    fun moveUpLeft() {
        moveTo(Coordinate(coordinate.x - 1, coordinate.y + 1))
    }

    fun moveUpRight() {
        moveTo(Coordinate(coordinate.x + 1, coordinate.y + 1))
    }

    fun moveDown() {
        moveTo(Coordinate(coordinate.x, coordinate.y - 1))
    }

    fun moveDownLeft() {
        moveTo(Coordinate(coordinate.x - 1, coordinate.y - 1))
    }

    fun moveDownRight() {
        moveTo(Coordinate(coordinate.x + 1, coordinate.y - 1))
    }

    fun moveRight() {
        moveTo(Coordinate(coordinate.x + 1, coordinate.y))
    }

    fun moveLeft() {
        moveTo(Coordinate(coordinate.x - 1, coordinate.y))
    }

    private fun moveTo(toCoord: Coordinate) {
        this.coordinate = toCoord
        visited.add(toCoord)
    }
}

data class Coordinate(var x: Int, var y: Int) {}