package dayX3

import java.io.File

sealed class Operation {
    data object Do : Operation()
    data object DoNot : Operation()
    data class Mul(val num1: Long, val num2: Long) : Operation()

    companion object {
        // "do()" || "don't()" || "mul(num1,num2)"
        val OP_REGEX = "(do|don't|mul)\\((?:(\\d+),(\\d+))?\\)".toRegex()

        fun fromMatch(matchResult: MatchResult): Operation {
            val (_, opString, num1, num2) = matchResult.groupValues
            return when (opString) {
                "do" -> Do
                "don't" -> DoNot
                "mul" -> Mul(num1.toLong(), num2.toLong())
                else -> throw RuntimeException("unknown operation $opString")
            }
        }
    }
}


fun preProcessData(text: String): List<Operation> =
    Operation.OP_REGEX.findAll(text).map(Operation::fromMatch).toList()

fun part1(operations: List<Operation>): Long =
    operations
        .filterIsInstance<Operation.Mul>()
        .stream()
        .mapToLong { it.num1 * it.num2 }
        .sum()

fun part2(operations: List<Operation>): Long {
    var sumProducts = 0L
    var enabled = true
    for (op in operations) {
        when (op) {
            is Operation.Do -> enabled = true
            is Operation.DoNot -> enabled = false
            is Operation.Mul -> {
                if (enabled) {
                    sumProducts += op.num1 * op.num2
                }
            }
        }
    }
    return sumProducts
}


fun main() {
    val input = File("inputs/day03.txt").readText()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 169021493
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 111762583
}