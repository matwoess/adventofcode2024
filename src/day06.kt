package day06

import util.Grid2D
import util.Direction
import util.Grid2D.Position
import java.io.File

fun preProcessData(arrayString: String): Grid2D<Char> {
    return Grid2D(arrayString, elemDelimiter = "")
}

fun turnRight(curDir: Direction) = when (curDir) {
    Direction.N -> Direction.E
    Direction.E -> Direction.S
    Direction.S -> Direction.W
    Direction.W -> Direction.N
    else -> throw IllegalArgumentException("Invalid start direction")
}

fun Grid2D<Char>.toStringWithHighlight(highlightedPos: Position<Char>): String {
    val (highlightRow, highlightCol) = highlightedPos.row to highlightedPos.col
    val printString = this.toString()

    val builder: StringBuilder = StringBuilder()
    for ((i, row) in printString.split("\r\n").withIndex()) {
        for ((j, col) in row.split(" ").withIndex()) {
            if (i == highlightRow && j == highlightCol) {
                builder.append("\u001B[31m@\u001B[0m") // print red @ instead
            } else {
                builder.append(col)
            }
        }
        builder.append("\r\n")
    }
    return builder.toString()
}

fun part1(grid: Grid2D<Char>): Int {
    var guardPos = grid.getPositions()
        .filter { it.el == '^' }
        .first()
    var curDir = Direction.N
    val visited = mutableSetOf(guardPos)
    while (true){
        val curSeq = grid.getDirectionalPositionSequence(guardPos, curDir)
        val newVisited = curSeq.toList().stream()
            .skip(1)
            .takeWhile { it.el == '.' || it.el == '^' }
            .toList()
        visited.addAll(newVisited.toSet())
        guardPos = newVisited.last()
        //println(grid.toStringWithHighlight(guardPos))
        val nextChar = grid.getAdjacentValue(guardPos, curDir)
        if (nextChar == null) {
            break
        } else if (nextChar == '#') {
            curDir = turnRight(curDir)
        }
    }
    return visited.size
}

fun part2(originalGrid: Grid2D<Char>): Int {
    val originalGuardPos = originalGrid.getPositions()
        .filter { it.el == '^' }
        .first()

    var loops = 0
    // brute force all possible positions to add 'O' and check for loops
    for (p in originalGrid.getPositions().filter { it.el == '.' }) {
        print("Checking with additional 'O' at $p\r")
        val grid = originalGrid.copyWithModification(p, 'O')
        val visited = mutableSetOf(originalGuardPos)
        var curDir = Direction.N
        var guardPos = originalGuardPos
        var iteration = 0
        while (true) {
            iteration++
            if (iteration >= 500) {
                //println("Loop found")
                loops++
                break
            }
            val curSeq = grid.getDirectionalPositionSequence(guardPos, curDir)
            val newVisited = curSeq.toList().stream()
                .takeWhile { it.el == '.' || it.el == '^' }
                .toList()
            visited.addAll(newVisited.toSet())
            guardPos = newVisited.last()
            val nextChar = grid.getAdjacentValue(guardPos, curDir)
            if (nextChar == null) {
                //println("No loop")
                break
            } else if (nextChar == '#' || nextChar == 'O') {
                curDir = turnRight(curDir)
            }
        }
    }
    return loops
}

fun main() {
    val input = File("inputs/day06.txt").readText()
    val data = preProcessData(input)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 5208
    assert(answer1 == 5208)
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 1972
    assert(answer2 == 1972)
}