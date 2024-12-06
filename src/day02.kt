package day02

import java.io.File

typealias Report = List<Int>

fun preProcessData(lines: List<String>): List<Report> {
    return lines.map { line -> line.split(" ").map { it.toInt() } }
}

fun isAscending(report: Report, maxDiff: Int = 3): Boolean {
    var currLevel = report[0]
    for (nextLevel in report.stream().skip(1)) {
        if (currLevel >= nextLevel || nextLevel - currLevel > maxDiff) {
            return false
        }
        currLevel = nextLevel
    }
    return true
}

fun isDescending(report: Report, maxDiff: Int = 3): Boolean {
    var currLevel = report[0]
    for (nextLevel in report.stream().skip(1)) {
        if (currLevel <= nextLevel || currLevel - nextLevel > maxDiff)
            return false
        currLevel = nextLevel
    }
    return true
}

fun part1(data: List<Report>): Int {
    return data.stream()
        .filter { isDescending(it) || isAscending(it) }
        .count().toInt()
}

fun part2(data: List<Report>): Int {
    var saveReports = 0
    for (report in data) {
        if (isDescending(report) || isAscending(report)) {
            saveReports++
        } else {
            for (i in report.indices) {
                // list with index i removed
                val subReport = report.subList(0, i) + report.subList(i + 1, report.size)
                if (isDescending(subReport) || isAscending(subReport)) {
                    saveReports++
                    break
                }
            }
        }
    }
    return saveReports
}


fun main() {
    val input = File("inputs/day02.txt").readLines()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 407
    assert(answer1 == 407)
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 459
    assert(answer2 == 459)
}