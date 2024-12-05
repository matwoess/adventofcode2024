package day05

import java.io.File

data class OrderRule(val page: Int, val before: Int) {
    companion object {
        fun fromString(input: String): OrderRule {
            val (page, before) = input.split("|")
            return OrderRule(page.toInt(), before.toInt())
        }
    }
}

data class PageSeq(val pages: List<Int>) {
    fun getMiddlePageNumber(): Int = pages[pages.size / 2]

    fun isValidForRules(orderRuleMap: Map<Int, Set<Int>>): Boolean {
        val numbersBeen = mutableSetOf<Int>()
        for (page in pages) {
            val mustBeBefore = orderRuleMap[page]
            if (mustBeBefore != null) {
                if (mustBeBefore.intersect(numbersBeen).isNotEmpty()) {
                    println("$page: invalid because of the intersection ${mustBeBefore.intersect(numbersBeen)}")
                    return false
                }
            }
            numbersBeen += page
        }
        return true
    }

    fun patchSequence(orderRuleMap: Map<Int, Set<Int>>): PageSeq {
        val numbersBeen = mutableSetOf<Int>()
        for ((index, page) in pages.withIndex()) {
            val mustBeBefore = orderRuleMap[page]
            if (mustBeBefore != null) {
                val intersection = mustBeBefore.intersect(numbersBeen)
                if (intersection.isNotEmpty()) {
                    println("$page: invalid because of the intersection $intersection")
                    val newPages = pages.subList(0, index+1).stream().filter { !intersection.contains(it) }.toList().toMutableList()
                    newPages += intersection.toList()
                    newPages += pages.subList(index + 1, pages.size)
                    val newSeq = PageSeq(newPages)
                    return newSeq
                }
            }
            numbersBeen += page
        }
        return this

    }

    companion object {
        fun fromString(input: String): PageSeq {
            return PageSeq(input.split(",").map(String::toInt))
        }
    }
}

fun preProcessData(input: String): Pair<List<OrderRule>, List<PageSeq>> {
    val (ruleString, pageString) = input.split("\n\n", "\r\n\r\n");
    val orderRules = ruleString.lines().map { OrderRule.fromString(it) }
    val pageSequences = pageString.lines().map { PageSeq.fromString(it) }
    return Pair(orderRules, pageSequences)
}

private fun getOrderRuleMap(orderRules: List<OrderRule>): Map<Int, Set<Int>> {
    val ruleMap: MutableMap<Int, MutableSet<Int>> = mutableMapOf()
    for ((page, before) in orderRules) {
        val set = ruleMap[page]
        if (set == null) {
            ruleMap[page] = mutableSetOf(before)
        } else {
            set += before
        }
    }
    return ruleMap.toMap()
}

fun part1(orderRules: List<OrderRule>, pageSequences: List<PageSeq>): Int {
    val orderRuleMap: Map<Int, Set<Int>> = getOrderRuleMap(orderRules)
    var validSeqMiddleNumberSum = 0
    println(orderRuleMap)
    for (pageSeq in pageSequences) {
        println("Checking sequence $pageSeq")
        if (pageSeq.isValidForRules(orderRuleMap)) {
            validSeqMiddleNumberSum += pageSeq.getMiddlePageNumber()
        }
    }
    return validSeqMiddleNumberSum
}

fun part2(orderRules: List<OrderRule>, pageSequences: List<PageSeq>): Int {
    val orderRuleMap: Map<Int, Set<Int>> = getOrderRuleMap(orderRules)
    val invalidSequences = pageSequences.filter { !it.isValidForRules(orderRuleMap) }
    var middleNumberSum = 0
    for (invalidSeq in invalidSequences) {
        println("Checking sequence $invalidSeq")
        var newSeq = invalidSeq.patchSequence(orderRuleMap)
        println("New sequence $newSeq")
        while (!newSeq.isValidForRules(orderRuleMap)) {
            newSeq = newSeq.patchSequence(orderRuleMap)
            println("Newer sequence $newSeq")
        }
        middleNumberSum += newSeq.getMiddlePageNumber()
    }
    return middleNumberSum
}


fun main() {
    val input = File("inputs/day05.txt").readText()
    val (rules, pages) = preProcessData(input)
    val answer1 = part1(rules, pages)
    println("Answer 1: $answer1") // 5064
    val answer2 = part2(rules, pages)
    println("Answer 2: $answer2") // 5152
}