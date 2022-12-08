import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val filePath = Paths.get("").toAbsolutePath().toString() + "\\" + args[0]
    var sum = 0
    File(filePath).forEachLine { line ->
        sum += calcScoreForRound(line)
    }
    println("final score is $sum")
}

fun calcScoreForRound(round: String): Int {
    val enemyMoveAndMyMove = round.split(" ")
    val enemyMove = enemyMoveAndMyMove[0][0]
    val myMove = enemyMoveAndMyMove[1][0]

    var outcomeScore = getOutcomeScorePart2(myMove)
    return getShapeScoreOfMyMovePart2(enemyMove, myMove) + outcomeScore
}

fun getShapeScoreOfMyMovePart2(enemyMove: Char, myMove: Char): Int {
    val enemyScore = getShapeScoreOfEnemyMove(enemyMove)
    val outcomeScore = getOutcomeScorePart2(myMove)

    // Draw...
    if (outcomeScore == 3) {
        return enemyScore
    }
    // Win!
    if (outcomeScore == 6) {
        return (enemyScore % 3) + 1
    }
    // Lose.. :(
    var diff = enemyScore - 1
    if (diff == 0) {
        return 3
    }
    return diff
}

fun getOutcomeScorePart2(myMove: Char): Int {
    return (myMove.code - 88) * 3
}

fun getOutcomeScore(enemyMove: Char, myMove: Char): Int {
    val enemyScore = getShapeScoreOfEnemyMove(enemyMove)
    val myScore = getShapeScoreOfMyMove(myMove)

    val diff = enemyScore - myScore
    // Draw
    if (diff == 0) {
        return 3
    }
    // Win
    if (diff == -1 || diff == 2) {
        return 6
    }
    // Lose
    return 0
}

fun getShapeScoreOfMyMove(move: Char): Int {
    return move.code - 87
}

fun getShapeScoreOfEnemyMove(move: Char): Int {
    return move.code - 64
}