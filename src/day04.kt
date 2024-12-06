package day04

import util.Grid2D
import util.Direction
import java.io.File

fun preProcessData(arrayString: String): Grid2D<Char> {
    return Grid2D(arrayString, elemDelimiter = "")
}

fun sequenceStartsWith(seq: Sequence<Char>, start: CharSequence): Boolean {
    return seq.take(start.length).joinToString("") == start
}

fun part1(grid: Grid2D<Char>): Int {
    val xPositions = grid.getPositions()
        .filter { it.el == 'X' }
        .toList()
    var xmasSequences = 0
    for (xPos in xPositions) {
        for (dir in Direction.entries) {
            val dirSeq = grid.getDirectionalSequence(xPos, dir)
            if (sequenceStartsWith(dirSeq, "XMAS")) {
                xmasSequences++
            }
        }
    }
    return xmasSequences
}

fun part2(grid: Grid2D<Char>): Int {
    val aPositions = grid.getPositions()
        .filter { it.el == 'A' }
        .toList()
    var xmasCrosses = 0
    for (aPos in aPositions) {
        val nw = grid.getAdjacentValue(aPos, Direction.NW)
        val ne = grid.getAdjacentValue(aPos, Direction.NE)
        val sw = grid.getAdjacentValue(aPos, Direction.SW)
        val se = grid.getAdjacentValue(aPos, Direction.SE)
        val masDiagonal1 = (nw == 'M' && se == 'S') || (nw == 'S' && se == 'M')
        val masDiagonal2 = (ne == 'M' && sw == 'S') || (ne == 'S' && sw == 'M')
        if (masDiagonal1 && masDiagonal2) {
            xmasCrosses++
        }
    }
    return xmasCrosses
}


fun main() {
    val input = File("inputs/day04.txt").readText()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 2517
    assert(answer1 == 2517)
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 1960
    assert(answer2 == 1960)
}