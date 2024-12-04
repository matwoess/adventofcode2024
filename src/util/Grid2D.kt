package util

class Grid2D<T>(input: String, elemDelimiter: String) {
    private val array: MutableList<MutableList<T>> = mutableListOf()
    private val height: Int
    private val width: Int

    init {
        val lines = input.lines()
        height = lines.size
        for (line in lines) {
            val row: MutableList<T> = mutableListOf()
            val entries = if (elemDelimiter == "") line.toCharArray().toList() else line.split(elemDelimiter)
            for (entry in entries) {
                row += entry as? T ?: throw ClassCastException("Cannot cast element to type T")
            }
            array += row
        }
        width = array[0].size
    }

    override fun toString(): String {
        return array.joinToString(separator = "\r\n") { line -> line.joinToString(separator = " ") }
    }

    data class Position<T>(val row: Int, val col: Int, val el: T)

    fun getPositions(): Sequence<Position<T>> {
        return sequence {
            for (row in array.indices) {
                for (col in array[row].indices) {
                    val el = Position(row, col, array[row][col])
                    yield(el)
                }
            }
        }
    }

    private fun isValidPosition(row: Int, col: Int): Boolean {
        return row in 0..<height && col in 0..<width
    }

    fun getDirectionalSequence(pos: Position<T>, dir: Direction): Sequence<T> {
        var currX = pos.col
        var currY = pos.row
        val (xOffset, yOffset) = dir.directionalOffset()
        return sequence {
            while (isValidPosition(currY, currX)) {
                yield(array[currY][currX])
                currX += xOffset
                currY += yOffset
            }
        }
    }
}