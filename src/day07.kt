package day07
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

enum class Operator(symbol: String) {
    ADD("+"), MULTIPLY("*"), CONCAT("||");

    val symbol: String?

    init {
        this.symbol = symbol
    }

    fun apply(x: Long, y: Long): Long {
        return when (this) {
            Operator.ADD -> x + y
            Operator.MULTIPLY -> x * y
            Operator.CONCAT -> (x.toString() + y.toString()).toLong()
        }
    }
}

data class Equation(val testValue: Long, val operands: MutableList<Long>?) {
    fun findPossibleResults(operators: Array<Operator>): MutableList<Long> {
        val firstValue: Long = operands.getFirst()
        val results: MutableList<Long> = ArrayList<Long>()
        searchValue(firstValue, 1, results, operators)
        return results
    }

    fun searchValue(current: Long, nextIndex: Int, results: MutableList<Long>, operators: Array<Operator>) {
        if (nextIndex == operands!!.size) {
            results.add(current)
            return
        }
        val nextVal = operands.get(nextIndex)
        for (operator in operators) {
            val newValue = operator.apply(current, nextVal)
            searchValue(newValue, nextIndex + 1, results, operators)
        }
    }
}

fun preProcessData(lines: MutableList<String>): MutableList<Equation> {
    val equations: MutableList<Equation> = ArrayList<Equation>()
    for (line in lines) {
        val testValue: Long = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toLong()
        val operands = Arrays.stream<String>(line.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1].trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()).map<Long?> { s: String? -> s.toLong() }.toList()
        equations.add(Equation(testValue, operands))
    }
    return equations
}

fun getSumOfSolvableEquations(allEquations: MutableList<Equation>, allowedOperators: Array<Operator>): Long {
    var totalSum: Long = 0
    for (eq in allEquations) {
        val results = eq.findPossibleResults(allowedOperators)
        for (res in results) {
            if (res == eq.testValue) {
                totalSum += eq.testValue
                break
            }
        }
    }
    return totalSum
}

fun part1(equations: MutableList<Equation>): Long {
    return getSumOfSolvableEquations(equations, arrayOf<Operator>(Operator.ADD, Operator.MULTIPLY))
}

fun part2(equations: MutableList<Equation>): Long {
    return getSumOfSolvableEquations(equations, Operator.entries.toTypedArray())
}

fun main() {
    val input = File("inputs/day07.txt").readText()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 3312271365652
    assert(answer1 == 5208)
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 509463489296712
    assert(answer2 == 1972)
}

