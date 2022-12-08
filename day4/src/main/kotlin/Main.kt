import java.io.File
import java.nio.file.Paths
import kotlin.math.max

fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]
    var sumContains = 0
    var sumOverlap = 0
    File(filePath).forEachLine { line ->
        val elfPair = line.split(",")
        val assigments1 = elfPair[0].split("-")
        val assigments2 = elfPair[1].split("-")

        // Does assignments1 contain assignment 2 or vice versa?
        if (doesEitherAssignmentsContainTheOther(assigments1, assigments2)) {
            sumContains += 1
        }
        if (!areAssignmentsDisjoint(assigments1, assigments2)) {
            sumOverlap += 1
        }
    }
    println("sum containing is $sumContains")
    println("sum overlapping is $sumOverlap")
}

fun areAssignmentsDisjoint(tasks1: List<String>, tasks2: List<String>): Boolean {
    // Are they disjoint completly?
    val start = 0
    val end = 1
    if (tasks1[end].toInt() < tasks2[start].toInt() || tasks2[end].toInt() < tasks1[start].toInt()) {
        // disjoint..
        return true
    }
    return false
}

fun doesEitherAssignmentsContainTheOther(tasks1: List<String>, tasks2: List<String>): Boolean {
    return doesAssignmentsContainOther(tasks1, tasks2) || doesAssignmentsContainOther(tasks2, tasks1)
}

fun doesAssignmentsContainOther(tasks1: List<String>, tasks2: List<String>): Boolean {
    val start = 0
    val end = 1
    return tasks1[start].toInt() <= tasks2[start].toInt() && tasks1[end].toInt() >= tasks2[end].toInt()
}

fun printRanges(tasks1: List<String>, tasks2: List<String>) {
    val end1 = tasks1[1].toInt()
    val end2 = tasks2[1].toInt()
    val maxValueToPrint = max(end1, end2) + 1
    // First line
    printRange(tasks1, maxValueToPrint)
    println("")
    // Second line
    printRange(tasks2, maxValueToPrint)
    println("")
    println("")
}

fun printRange(tasks: List<String>, maxValueToPrint: Int) {
    val start = tasks[0].toInt()
    val end = tasks[1].toInt()
    for (i in 0..maxValueToPrint) {
        if (i < start || i > end) {
            print(".")
            if (i > 9) {
                print(".")
            }
        } else {
            print(i)
        }
    }
}