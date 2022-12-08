import java.io.File
import java.nio.file.Paths
import java.util.Collections.min

fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]

    // Build tree
    val root = MyDir()
    root.name = "/"
    var curDir = root
    File(filePath).forEachLine { line ->
        if (line == "$ cd /") {
            return@forEachLine
        }
        val elems = line.split(" ")
        if (elems[0] == "$") {
            // Command... cd x or ls
            if (elems[1] == "ls") {
                return@forEachLine
            }
            if (elems[1] == "cd") {
                curDir = curDir.moveIntoDir(elems[2])
                return@forEachLine
            }
        }
        // We are creating dir or file
        if (elems[0] == "dir") {
            curDir.addDir(elems[1])
        } else {
            curDir.addFile(elems[0].toInt(), elems[1])
        }
    }
    val maxSize = 100000
    val curFreeSpace = 70000000 - root.getSize()
    val neededSpace = 30000000
    val smallestSizeToDelete = neededSpace - curFreeSpace

    println("Sum of all dir sizes less than $maxSize is ${getSumSizesLessThanX(root, maxSize)}")
    println("Free space: $curFreeSpace")
    println("Need to free up at least $smallestSizeToDelete")
    println("found ${findSmallestDirectoryToDelete(root, smallestSizeToDelete)}")
}

fun findSmallestDirectoryToDelete(root: MyDir, x: Int): Int {
    val curSize = root.getSize()
    val candidates: MutableList<Int> = mutableListOf()
    if (curSize >= x) {
        candidates.add(curSize)
    }
    for (dir in root.directories) {
        candidates.add(findSmallestDirectoryToDelete(dir, x))
    }
    // Not always a directory that were added.
    return try {
        min(candidates)
    } catch (e: NoSuchElementException) {
        Int.MAX_VALUE
    }
}

fun getSumSizesLessThanX(root: MyDir, x: Int): Int {
    val size = root.getSize()
    var tmp = 0
    if (size <= x) {
        tmp += size
    }
    for (dir in root.directories) {
        tmp += getSumSizesLessThanX(dir, x)
    }
    return tmp
}

class MyDir {
    var name = ""
    var files: MutableList<MyFile> = mutableListOf()

    // Of course this should be a hashmap
    var directories: MutableList<MyDir> = mutableListOf()
    var parent: MyDir? = null

    fun moveIntoDir(name: String): MyDir {
        if (name == "..") {
            if (parent != null) {
                return parent as MyDir
            } else {
                throw Exception("Trying to move out of directory with no parent..")
            }
        }
        for (dir in directories) {
            if (dir.name == name) {
                return dir
            }
        }
        throw Exception("No dir of name $name")
    }

    fun addDir(name: String) {
        val newDir = MyDir()
        newDir.name = name
        newDir.parent = this
        directories.add(newDir)
    }

    fun addFile(size: Int, name: String) {
        val file = MyFile()
        file.name = name
        file.size = size
        files.add(file)
    }

    fun getSize(): Int {
        var sum = 0
        for (file in files) {
            sum += file.size
        }
        for (dir in directories) {
            sum += dir.getSize()
        }
        return sum
    }

    fun print() {
        println("${this.name} contains file $files and dirs $directories")
        for (dir in this.directories) {
            dir.print()
        }
    }

    override fun toString(): String {
        return name
    }
}

class MyFile {
    var name = ""
    var size = 0
    override fun toString(): String {
        return "$name $size"
    }
}