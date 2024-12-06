package day01

import java.io.File
import kotlin.math.abs


fun preProcessData(lines: List<String>): Pair<List<Int>, List<Int>> {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    for (line in lines) {
        val numbers = line.split("   ")
        list1.add(numbers[0].toInt())
        list2.add(numbers[1].toInt())
    }
    return Pair(list1, list2)
}

fun part1(data: Pair<List<Int>, List<Int>>): Int {
    val list1 = data.first.sorted()
    val list2 = data.second.sorted()
    var totalDifference = 0
    for (i in list1.indices) {
        val difference = abs(list1[i] - list2[i])
        totalDifference += difference
    }
    return totalDifference
}

fun part2(data: Pair<List<Int>, List<Int>>): Int {
    val list1 = data.first.sorted()
    val list2 = data.second.sorted()
    var similarityScore = 0
    for (num in list1) {
        val occurrences = list2.count { it == num }
        similarityScore += num * occurrences
    }
    return similarityScore
}


fun main() {
    val input = File("inputs/day01.txt").readLines()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 1580061
    assert(answer1 == 1580061)
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 23046913
    assert(answer2 == 23046913)
}