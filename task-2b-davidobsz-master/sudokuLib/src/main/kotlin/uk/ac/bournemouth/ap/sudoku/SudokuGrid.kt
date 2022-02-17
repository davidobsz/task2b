package uk.ac.bournemouth.ap.sudoku

import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import uk.ac.bournemouth.ap.sudoku.SudokuGrid.SudokuCell

class SudokuGrid : Iterable<SudokuCell> {

    private val grid: Matrix<SudokuCell> = MutableMatrix(9, 9, ::SudokuCell)

    operator fun get(x: Int, y: Int) = grid[x, y]

    override fun iterator() = grid.iterator()

    fun isValid(): Boolean {
        for (column in columns) {
            if (!isColumnValid(column)) return false
        }
        for (row in rows) {
            if (!isRowValid(row)) return false
        }
        for (group in groups) {
            if (!isGroupValid(group)) return false
        }
        return true
    }

    private fun isColumnValid(column: Int): Boolean {
        return rows.map { row -> Coordinate(column, row) }.hasNoDuplicates()
    }

    private fun isRowValid(row: Int): Boolean {
        return columns.map { col -> Coordinate(col, row) }.hasNoDuplicates()
    }

    private fun isGroupValid(group: Group): Boolean {
        return group.cells.hasNoDuplicates()
    }

    private fun Iterable<Coordinate>.hasNoDuplicates(): Boolean {
        val seen = BooleanArray(10)

        // This runs against the "receiver" so each coordinate
        forEach { (x, y) ->
            val value = grid[x, y].number
            if (value != EMPTY) {
                if (seen[value]) return false
                seen[value] = true
            }
        }
        return true
    }

    inner class SudokuCell(val x: Int, val y: Int) {
        var number: Int = 0
            set(value) {
                if (value !in 0..8) throw IllegalArgumentException("Not in range: $value")
                field = value
            }

        val group= Group(x / 3, y / 3)

        fun isValid(): Boolean {
            return isColumnValid(x) && isRowValid(y) && isGroupValid(group)
        }

        private fun otherColumns() = columns.filter { it != x }
        private fun otherRows() = rows.filter { it != y }
        private fun otherCellsInGroup() = group.cells.filter { it.x!=x || it.y !=y }

        fun calculateAvailable(): List<Int> {
            val seen = BooleanArray(10)
            for (col in otherColumns()) {
                val v = get(col, y).number
                seen[v] = true
            }

            for (row in otherRows()) {
                val v = get(row, x).number
                seen[v] = true
            }

            for ( (col, row) in otherCellsInGroup()) {
                val v = get(col, row).number
                seen[v] = true
            }

            return (1..9).filter { !seen[it] }
        }
    }

    companion object {
        const val EMPTY = 0

        val rows: IntRange = 0..8
        val columns: IntRange = 0..8
        val groups: Array<Group> = (0..2).flatMap { x -> (0..2).map { y -> Group(x, y) } }.toTypedArray()

    }
}

inline class Group(private val groupNo: Int) {
    constructor(x: Int, y: Int) : this(x + y * 3)

    val x get() = groupNo % 3
    val y get() = groupNo / 3

    val gridX get() = x * 3
    val gridY get() = y * 3

    val cells: List<Coordinate> get() {
        val gridX = gridX
        val gridY = gridY

        return (0..2).flatMap { x ->
            (0..2).map { y->
                Coordinate(gridX + x, gridY + y)
            }
        }
    }
}