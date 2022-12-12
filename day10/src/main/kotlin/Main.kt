import java.io.File
import java.nio.file.Paths
import kotlin.math.abs

fun main(args: Array<String>) {
    val fileName = "Input.txt"
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + fileName
    val instructions = File(filePath).readLines()

    val cpu = CPU(instructions)
    cpu.run()
    println("Sum of signal strengths: ${cpu.getSumOfSignalStrengths()}")
}

class CPU(val instructions: List<String>) {
    private var programCounter = 0
    private var cycle = 0
    private var registerX = 1
    private var tmpRegister = 0
    private val interestingCycles = listOf(20, 60, 100, 140, 180, 220)
    private val signalStrengths: MutableList<Int> = mutableListOf()
    private val screenWidth = 40

    private fun executeCycle() {
        if (cycle in interestingCycles) {
            signalStrengths.add(cycle * registerX)
        }
        if (tmpRegister != 0) {
            registerX += tmpRegister
            tmpRegister = 0
            programCounter += 1
        } else if (instructions[programCounter] == "noop") {
            programCounter += 1
        } else {
            val commandAndParameter = instructions[programCounter].split(" ")
            val command = commandAndParameter[0]
            val parameter = commandAndParameter[1]
            if (command == "addx") {
                tmpRegister = parameter.toInt()
            }
        }
    }

    fun run() {
        while (!isFinished()) {
            cycle += 1
            printCurrentPixel()
            executeCycle()
        }
    }

    fun isFinished() = programCounter == instructions.size

    fun printState() = println("CPU is in cycle $cycle with register $registerX")

    fun printCurrentPixel() {
        val curScreenPos = (cycle - 1) % screenWidth
        if (abs(curScreenPos - registerX) <= 1) {
            print("#")
        } else {
            print(".")
        }
        if (cycle % screenWidth == 0) {
            println("")
        }
    }

    fun getSumOfSignalStrengths(): Int {
        return signalStrengths.sum()
    }
}