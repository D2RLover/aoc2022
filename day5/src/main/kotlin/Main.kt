import java.io.File
import java.nio.file.Paths

const val numberOfStacks = 9

// 2, 6, 10, 14...
fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]
    val startingStacksAndProcedure = File(filePath).readText().split("\r\n\r\n")
    var data = buildDataStructureFromFile(startingStacksAndProcedure[0])

    println(data)

    // Run the procedure

    var procedure = startingStacksAndProcedure[1].split("\r\n")
    for (move in procedure) {
        val tmp = move.split(" ")
        val amount = tmp[1].toInt()
        val fromStack = tmp[3].toInt() - 1 // we use 0 indexing in the real world
        val toStack = tmp[5].toInt() - 1

        var tmpDeq: ArrayDeque<Char> = ArrayDeque()
        for (i in 1..amount) {
            tmpDeq.addLast(data[fromStack].removeLast())
        }
        for (i in 1..amount) {
            data[toStack].addLast(tmpDeq.removeLast())
        }
    }
    for (stack in data) {
        print(stack.last())
    }
    println("")
}

fun buildDataStructureFromFile(startingStacks: String): MutableList<ArrayDeque<Char>> {
    val dequeList: MutableList<ArrayDeque<Char>> = mutableListOf()
    for (i in 1..numberOfStacks) {
        dequeList.add(ArrayDeque())
    }
    var nextIndexIsChar = false
    var curindex = 0
    startingStacks.forEach { c ->
        // Add current line to bottom of deque, fill it bottom up
        if (nextIndexIsChar) {
            val stackIndex = getStackIndexFromLineIndex(curindex)
            // do stuff
            dequeList[stackIndex].addFirst(c)
            nextIndexIsChar = false
        }
        if (c == '[') {
            nextIndexIsChar = true
        }
        curindex += 1
        if (c == '\r') {
            curindex = 0
            nextIndexIsChar = false
        }
    }
    return dequeList
}

fun getStackIndexFromLineIndex(x: Int): Int {
    var tmpX = x
    var y = 0
    while (tmpX >= 4) {
        tmpX -= 4
        y += 1
    }
    return y
}