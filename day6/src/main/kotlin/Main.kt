import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]
    val cmd = File(filePath).readText()

    var sb = StringBuilder()
    val uniqueChars = 14
    for (c in cmd) {
        sb.append(c)
        if (sb.length < uniqueChars) {
            continue
        }
        if (checkLastXAreDifferent(sb.toString(), uniqueChars)) {
            println("Found all different.. at pos: ${sb.length}")
            break
        }
    }
}

fun checkLastXAreDifferent(line: String, x: Int): Boolean {
    var tmp = line.substring(line.length - x, line.length).toList()
    tmp = tmp.distinct()
    if (tmp.size == x) {
        return true
    }
    return false
}