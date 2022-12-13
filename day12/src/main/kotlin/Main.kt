import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    println(partA("testInput.txt"))
    println(partA("Input.txt"))

    println(partB("testInput.txt"))
    println(partB("Input.txt"))
}

fun partA(fileName: String): Int? {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + fileName
    val input = File(filePath).readLines()

    val maxSizeY = input.size - 1
    val maxSizeX = input.first().length
    val map = buildMap(input)
    val startPos = getPosOfChar(input, 'E')
    val endPos = getPosOfChar(input, 'S')

    val smallestDists = findSmallestDistancesMapFromStartingPoint(map, startPos, maxSizeY, maxSizeX)
    return smallestDists[endPos]
}

fun partB(fileName: String): Int? {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + fileName
    val input = File(filePath).readLines()

    val maxSizeY = input.size - 1
    val maxSizeX = input.first().length
    val map = buildMap(input)
    val endingPositions = getAllStartingPositions(input)
    val startPos = getPosOfChar(input, 'E')

    val smallestDists = findSmallestDistancesMapFromStartingPoint(map, startPos, maxSizeY, maxSizeX)

    var currentMin = Int.MAX_VALUE
    for (endPos in endingPositions) {
        if (smallestDists[endPos] != null) {
            if (currentMin > smallestDists[endPos]!!) {
                currentMin = smallestDists[endPos]!!
            }
        }
    }
    return currentMin
}

fun getPosOfChar(input: List<String>, char: Char): Pair<Int, Int> {
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == char) {
                return Pair(x, y)
            }
        }
    }
    error("no pos found")
}

fun getAllStartingPositions(input: List<String>): List<Pair<Int, Int>> {
    val startingPositions = mutableListOf<Pair<Int, Int>>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == 'a' || input[y][x] == 'S') {
                startingPositions.add(Pair(x, y))
            }
        }
    }
    return startingPositions
}

fun findSmallestDistancesMapFromStartingPoint(
    map: Map<Pair<Int, Int>, Char>,
    startPos: Pair<Int, Int>,
    maxSizeY: Int,
    maxSizeX: Int
): Map<Pair<Int, Int>, Int> {
    val currentBest = mutableMapOf<Pair<Int, Int>, Int>()
    val queue = ArrayDeque<Pair<Pair<Int, Int>, Int>>()
    queue.add(Pair(startPos, 0))

    while (queue.size > 0) {
        val cur = queue.removeLast()
        val curPos = cur.first
        val curValue = cur.second
        val possibleMoves = getPossibleMoves(map, curPos, maxSizeX, maxSizeY)

        for (newPos in possibleMoves) {
            if (!currentBest.contains(newPos)) {
                currentBest[newPos] = curValue + 1
                queue.addLast(Pair(newPos, curValue + 1))
            } else {
                if (currentBest[newPos]!! > curValue + 1) {
                    currentBest[newPos] = curValue + 1
                    queue.addLast(Pair(newPos, curValue + 1))
                }
            }
        }
    }
    return currentBest
}

fun getPossibleMoves(
    map: Map<Pair<Int, Int>, Char>,
    curPos: Pair<Int, Int>,
    maxSizeX: Int,
    maxSizeY: Int
): List<Pair<Int, Int>> {
    val possibleMoves = mutableListOf<Pair<Int, Int>>()
    val leftPos = Pair(curPos.first - 1, curPos.second)
    if (isPossibleMove(map, curPos, leftPos, maxSizeY, maxSizeX)) {
        possibleMoves.add(leftPos)
    }
    val rightPos = Pair(curPos.first + 1, curPos.second)
    if (isPossibleMove(map, curPos, rightPos, maxSizeY, maxSizeX)) {
        possibleMoves.add(rightPos)
    }
    val upPos = Pair(curPos.first, curPos.second - 1)
    if (isPossibleMove(map, curPos, upPos, maxSizeY, maxSizeX)) {
        possibleMoves.add(upPos)
    }
    val downPos = Pair(curPos.first, curPos.second + 1)
    if (isPossibleMove(map, curPos, downPos, maxSizeY, maxSizeX)) {
        possibleMoves.add(downPos)
    }
    return possibleMoves
}

fun isPosOutsideMap(newPos: Pair<Int, Int>, maxSizeY: Int, maxSizeX: Int): Boolean {
    return newPos.first < 0 || newPos.first > maxSizeX || newPos.second < 0 || newPos.second > maxSizeY
}

fun isPossibleMove(
    map: Map<Pair<Int, Int>, Char>,
    curPos: Pair<Int, Int>,
    newPos: Pair<Int, Int>,
    maxSizeY: Int,
    maxSizeX: Int
): Boolean {
    if (isPosOutsideMap(newPos, maxSizeY, maxSizeX)) return false

    val curCharValue = getCharValue(map.getOrDefault(curPos, 'a'))
    val newCharValue = getCharValue(map.getOrDefault(newPos, 'a'))
    if (curCharValue - newCharValue > 1) {
        return false
    }
    return true
}

fun getCharValue(char: Char): Int {
    return when (char) {
        'E' -> 'z'.code
        'S' -> 'a'.code
        else -> char.code
    }
}

fun buildMap(input: List<String>): Map<Pair<Int, Int>, Char> {
    val map = mutableMapOf<Pair<Int, Int>, Char>()
    for (y in input.indices) {
        for (x in input[y].indices)
            map[Pair(x, y)] = input[y][x]
    }
    return map
}