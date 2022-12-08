import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]
    //solvePart1(File(filePath))
    solvePart2(File(filePath))
}

fun solvePart2(file: File) {
    var curIndex = 0
    var tableRow = 0
    val table: MutableList<MutableList<String>> = mutableListOf(mutableListOf())
    // Create some table
    file.forEachLine() { line ->
        if (curIndex == 3) {
            // Add new row..
            tableRow += 1
            curIndex = 0
            table.add(mutableListOf())
        }
        table[tableRow].add(line)
        curIndex += 1
    }

    var sum = 0;
    for (ruckSacks in table) {
        for (x in ruckSacks[0]) {
            if (ruckSacks[1].contains(x)) {
                val xRegex = x.toString().toRegex()
                ruckSacks[1] = ruckSacks[1].replace(xRegex, "")
                if (ruckSacks[2].contains(x)) {
                    // MATCH!
                    sum += getPriorityValue(x)
                    ruckSacks[2] = ruckSacks[2].replace(xRegex, "")
                }
            }
        }
    }
    println("sum is $sum")
}

fun solvePart1(file: File) {
    var sum = 0
    file.forEachLine { line ->
        // All compartments should be the same size.. so should be able to split in 2
        val firstComp = line.subSequence(0, line.length / 2)
        var secondComp = line.subSequence(line.length / 2, line.length)

        for (x in firstComp) {
            if (secondComp.contains(x)) {
                // Match!
                secondComp = secondComp.replace(x.toString().toRegex(), "")
                sum += getPriorityValue(x)
            }
        }
    }
    println("sum is: $sum")
}

fun getPriorityValue(char: Char): Int {
    if (char.isLowerCase()) {
        return char.code - 96
    }
    return char.code - 38
}
