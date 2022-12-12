import java.io.File
import java.math.BigInteger
import java.nio.file.Paths

fun main(args: Array<String>) {
    val fileName = "Input.txt"
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + fileName
    val input = File(filePath).readLines()

    var monkeys = createMonkeys(input)

    println("Monkey business part a: ${partA(monkeys)}")
    monkeys = createMonkeys(input)
    println("Monkey business part b: ${partB(monkeys)}")
}

fun partA(monkeys: List<Monkey>): BigInteger {
    val rounds = 20
    for (i in 1..rounds) {
        for (monkey in monkeys) {
            monkey.throwItemsToMonkeys(monkeys, 3, 0)
        }
    }
    val mutMonkeys = monkeys.toMutableList()
    return getTopMonkeyBusinessAndRemoveMonkey(mutMonkeys) * getTopMonkeyBusinessAndRemoveMonkey(mutMonkeys)
}

fun partB(monkeys: List<Monkey>): BigInteger {
    val rounds = 10_000
    var lcm = getLcmOfAllMonkeyDivisors(monkeys)
    for (i in 1..rounds) {
        for (monkey in monkeys) {
            monkey.throwItemsToMonkeys(monkeys, 1, lcm)
        }
    }
    val mutMonkeys = monkeys.toMutableList()
    return getTopMonkeyBusinessAndRemoveMonkey(mutMonkeys) * getTopMonkeyBusinessAndRemoveMonkey(mutMonkeys)
}

fun getLcmOfAllMonkeyDivisors(monkeys: List<Monkey>): Int {
    var lcm = monkeys.first().testDivisor
    while (true) {
        if (isLcm(monkeys, lcm)) {
            break
        } else {
            lcm += 1
        }
    }
    return lcm
}

fun isLcm(monkeys: List<Monkey>, lcm: Int): Boolean {
    for (monkey in monkeys) {
        if (lcm % monkey.testDivisor != 0) {
            return false
        }
    }
    return true
}

fun getTopMonkeyBusinessAndRemoveMonkey(monkeys: MutableList<Monkey>): BigInteger {
    var curMax = BigInteger.valueOf(0)
    var indexMax = 0
    for (i in monkeys.indices) {
        if (monkeys[i].inspectedItems > curMax) {
            curMax = monkeys[i].inspectedItems
            indexMax = i
        }
    }
    monkeys.removeAt(indexMax)
    return curMax
}

fun createMonkeys(input: List<String>): List<Monkey> {
    val monkeys: MutableList<Monkey> = mutableListOf()
    for (line in input) {
        if (line.isBlank()) {
            continue
        }
        if (line.contains("Monkey")) {
            monkeys.add(Monkey())
        } else if (line.contains("Starting items")) {
            val startingItems = line.trim().split(":")[1].split(",")
            for (item in startingItems) {
                monkeys.last().addItem(item.trim().toLong())
            }
        } else if (line.contains("Operation:")) {
            val tmp = line.split("=")[1].trimStart().split(" ")
            monkeys.last().operatorsLeft = tmp[0].trim()
            monkeys.last().operation = tmp[1].trim()
            monkeys.last().operatorRight = tmp[2].trim()
        } else if (line.contains("Test: divisible by")) {
            monkeys.last().testDivisor = line.split(" ").last().toInt()
        } else if (line.contains("If true")) {
            monkeys.last().throwToMonkeyTrue = line.split(" ").last().toInt()
        } else {
            monkeys.last().throwToMonkeyFalse = line.split(" ").last().toInt()
        }
    }
    return monkeys
}

class Monkey {
    val items: MutableList<Long> = mutableListOf()
    var inspectedItems = BigInteger.valueOf(0)
    var operation = ""
    var operatorsLeft = ""
    var operatorRight = ""
    var testDivisor = 1
    var throwToMonkeyFalse = -1
    var throwToMonkeyTrue = -1

    fun addItem(item: Long) {
        items.add(item)
    }

    fun throwItemsToMonkeys(monkeys: List<Monkey>, worryDivisor: Int, lcm: Int) {
        while (items.size != 0) {
            var worryLevel = items.removeFirst()
            val opRight = if (operatorRight == "old") {
                worryLevel
            } else {
                operatorRight.toLong()
            }
            val opLeft = if (operatorsLeft == "old") {
                worryLevel
            } else {
                operatorsLeft.toLong()
            }
            worryLevel = if (operation == "*") {
                opRight * opLeft
            } else {
                opRight + opLeft
            }
            worryLevel /= worryDivisor
            if (lcm != 0)
                worryLevel %= lcm

            // test
            if (worryLevel % testDivisor == 0L) {
                monkeys[throwToMonkeyTrue].addItem(worryLevel)
            } else {
                monkeys[throwToMonkeyFalse].addItem(worryLevel)
            }
            inspectedItems += BigInteger.valueOf(1)
        }
    }
}