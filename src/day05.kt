package day05

import java.io.File
import java.util.stream.Collectors

data class OrderRule(val page: Int, val successor: Int) {
    companion object {
        fun fromString(input: String): OrderRule {
            val (page, successor) = input.split("|")
            return OrderRule(page.toInt(), successor.toInt())
        }
    }
}

data class PageSeq(val pages: List<Int>) {
    companion object {
        fun fromString(input: String): PageSeq {
            return PageSeq(input.split(",").map(String::toInt))
        }
    }

    fun getMiddlePageNumber(): Int = pages[pages.size / 2]

    fun isValidForRules(successorRules: Map<Int, Set<Int>>): Boolean {
        val previousPages = mutableSetOf<Int>()
        for (page in pages) {
            val mustBeAfter = successorRules[page]
            if (mustBeAfter != null) {
                val invalidPredecessors = mustBeAfter.intersect(previousPages)
                if (invalidPredecessors.isNotEmpty()) {
                    return false
                }
            }
            previousPages += page
        }
        return true
    }

    fun patchInvalidSequence(successorRules: Map<Int, Set<Int>>, startIndex: Int = 0): PageSeq {
        val previousPages = pages.subList(0, startIndex).toMutableSet()
        for (curIdx in startIndex..<pages.size) {
            val page = pages[curIdx]
            val mustBeAfter = successorRules[page]
            if (mustBeAfter != null) {
                val invalidPredecessors = mustBeAfter.intersect(previousPages)
                if (invalidPredecessors.isNotEmpty()) {
                    // new list of pages where the invalid predecessors are moved to the right of the current index
                    val newPages = (pages.subList(0, curIdx + 1).filter { it !in invalidPredecessors }
                            + invalidPredecessors
                            + pages.subList(curIdx + 1, pages.size))
                    // create new sequence, try to patch it again recursively starting from an earlier index
                    // return first valid sequence automatically
                    return PageSeq(newPages).patchInvalidSequence(successorRules, curIdx - invalidPredecessors.size)
                }
            }
            previousPages += page
        }
        return this

    }
}

fun preProcessData(input: String): Pair<Map<Int, Set<Int>>, List<PageSeq>> {
    val (ruleString, pageString) = input.split("\n\n", "\r\n\r\n")
    val pageSequences = pageString.lines().map { PageSeq.fromString(it) }
    val rules = ruleString.lines().map { OrderRule.fromString(it) }
    val successorRules = rules.stream()
        .collect(
            Collectors.groupingBy(
                OrderRule::page,
                Collectors.mapping(OrderRule::successor, Collectors.toSet())
            )
        )
    return Pair(successorRules, pageSequences)
}


fun part1(successorRules: Map<Int, Set<Int>>, pageSequences: List<PageSeq>): Int {
    return pageSequences
        .filter { it.isValidForRules(successorRules) }
        .sumOf { it.getMiddlePageNumber() }
}

fun part2(successorRules: Map<Int, Set<Int>>, pageSequences: List<PageSeq>): Int {
    return pageSequences
        .filter { !it.isValidForRules(successorRules) }
        .map { it.patchInvalidSequence(successorRules) }
        .sumOf { it.getMiddlePageNumber() }
}


fun main() {
    val input = File("inputs/day05.txt").readText()
    val (rules, pages) = preProcessData(input)
    val answer1 = part1(rules, pages)
    println("Answer 1: $answer1") // 5064
    val answer2 = part2(rules, pages)
    println("Answer 2: $answer2") // 5152
}