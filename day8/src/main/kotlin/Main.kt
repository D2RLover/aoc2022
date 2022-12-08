import java.io.File
import java.lang.Character.isDigit
import java.nio.file.Paths

fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]
    val squareSize = File(filePath).readText().split("\r\n").size
    val input = File(filePath).readText().filter { isDigit(it) }

    val map = createHeightMap(input, squareSize)
    markVisibleTrees(map, squareSize)
    printMap(map)
    println("${countVisibleTrees(map)} trees are visible from outside the grid")
    println("Max scenic score of any tree is ${findMaxScenicScore(map, squareSize)}")
}

fun findMaxScenicScore(map: List<List<MyTree>>, squareSize: Int): Int {
    var maxScenicScore = 0
    for (y in 0 until squareSize) {
        for (x in 0 until squareSize) {
            val scenicScoreOfTree = getScenicScoreOfTree(map, squareSize, x, y)
            if (scenicScoreOfTree > maxScenicScore) {
                maxScenicScore = scenicScoreOfTree
            }
        }
    }
    return maxScenicScore
}

fun getScenicScoreOfTree(map: List<List<MyTree>>, squareSize: Int, initX: Int, initY: Int): Int {

    val curHeight = map[initY][initX].height
    var viewDistRight = 0
    for (x in initX + 1 until squareSize) {
        if (map[initY][x].height < curHeight) {
            viewDistRight += 1
        } else {
            viewDistRight += 1
            break
        }
    }
    var viewDistLeft = 0
    for (x in initX - 1 downTo 0) {
        if (map[initY][x].height < curHeight) {
            viewDistLeft += 1
        } else {
            viewDistLeft += 1
            break
        }
    }
    var viewDistDown = 0
    for (y in initY + 1 until squareSize) {
        if (map[y][initX].height < curHeight) {
            viewDistDown += 1
        } else {
            viewDistDown += 1
            break
        }
    }
    var viewDistUp = 0
    for (y in initY - 1 downTo 0) {
        if (map[y][initX].height < curHeight) {
            viewDistUp += 1
        } else {
            viewDistUp += 1
            break
        }
    }
    return viewDistRight * viewDistLeft * viewDistDown * viewDistUp
}

fun countVisibleTrees(map: List<List<MyTree>>): Int {
    var sum = 0
    for (line in map) {
        for (tree in line) {
            if (tree.visible) {
                sum += 1
            }
        }
    }
    return sum
}

fun markVisibleTrees(map: List<List<MyTree>>, squareSize: Int) {
    // From left to right and vice versa
    for (y in 0 until squareSize) {
        var curHeightLeftToRight = -1
        var curHeightRightToLeft = -1
        for (x in 0 until squareSize) {
            if (map[y][x].height > curHeightLeftToRight) {
                map[y][x].visible = true
                curHeightLeftToRight = map[y][x].height
            }
            if (map[y][squareSize - x - 1].height > curHeightRightToLeft) {
                map[y][squareSize - x - 1].visible = true
                curHeightRightToLeft = map[y][squareSize - x - 1].height
            }
        }
    }
    // From top to bottom and vice versa
    for (x in 0 until squareSize) {
        var curHeightBottomToTop = -1
        var curHeightTopToBottom = -1
        for (y in 0 until squareSize) {
            if (map[y][x].height > curHeightBottomToTop) {
                map[y][x].visible = true
                curHeightBottomToTop = map[y][x].height
            }
            if (map[squareSize - y - 1][x].height > curHeightTopToBottom) {
                map[squareSize - y - 1][x].visible = true
                curHeightTopToBottom = map[squareSize - y - 1][x].height
            }
        }
    }
}

fun getHeightOfTreeAtPos(x: Int, y: Int, map: String, squareSize: Int): Int {
    val tmpIndex = x + y * squareSize
    return map[tmpIndex].toString().toInt()
}

fun createHeightMap(input: String, squareSize: Int): List<List<MyTree>> {
    val map: MutableList<MutableList<MyTree>> = mutableListOf()
    for (y in 0 until squareSize) {
        map.add(mutableListOf())
        for (x in 0 until squareSize) {
            val tree = MyTree()
            tree.height = getHeightOfTreeAtPos(x, y, input, squareSize)
            map[y].add(tree)
        }
    }
    return map
}

fun printMap(map: List<List<MyTree>>) {
    for (line in map) {
        for (tree in line) {
            print(tree)
        }
        println()
    }
}

class MyTree {
    var height = -1
    var visible = false
    override fun toString(): String {
        return if (visible) "X" else "."
    }
}