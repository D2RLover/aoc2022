import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]
    // Read file and do calc
    val elvesAndTheirCalories: ArrayList<Int> = arrayListOf()
    var curElfCalorie = 0
    File(filePath).forEachLine { line ->
        if (line == "") {
            elvesAndTheirCalories.add(curElfCalorie)
            curElfCalorie = 0
        } else {
            curElfCalorie += line.toInt()
        }
    }
    // Need to add last elf's calories since we don't have an extra newline at the end.
    elvesAndTheirCalories.add(curElfCalorie)

    elvesAndTheirCalories.sortDescending()
    val maxElf = elvesAndTheirCalories[0]
    println("max calorie of any elf is $maxElf")

    println("Total calories of top 3 elves are ${maxElf + elvesAndTheirCalories[1] + elvesAndTheirCalories[2]}")
}