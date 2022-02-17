package uk.ac.bournemouth.ap.sudoku

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource


internal class SudokuGridTest {
    lateinit var defaultGrid: SudokuGrid

    @BeforeEach
    fun createTestGrid() {
        defaultGrid = SudokuGrid()
    }

    @Test
    fun testAvailableNumbers() {
        defaultGrid[0,0].number = 1
        defaultGrid[1,1].number = 2
        defaultGrid[0,5].number = 3
        defaultGrid[8,2].number = 4
        defaultGrid[1,3].number = 5

        val available = defaultGrid[0,2].calculateAvailable().toIntArray()
        assertArrayEquals(intArrayOf(5,6,7,8,9), available)
    }

    @Test
    fun assertEmptyGridOnStart() {
        val grid = SudokuGrid()
        assertTrue { grid.all { it.number == SudokuGrid.EMPTY } }
    }

    @Test
    fun testSetCells() {
        for (x in 0..8) {
            for (y in 0..8) {
                val value = (x * 31 + y) % 10
                defaultGrid[x, y].number = value
                assertEquals(value, defaultGrid[x, y].number)
            }
        }
    }

    @Test
    fun testSetCellsInvalidValue() {
        for (x in 0..8) {
            for (y in 0..8) {
                assertThrows<IllegalArgumentException> { defaultGrid[x, y].number = 10 }
                assertThrows<IllegalArgumentException> { defaultGrid[x, y].number = 20 }
                assertThrows<IllegalArgumentException> { defaultGrid[x, y].number = -1 }
                assertThrows<IllegalArgumentException> { defaultGrid[x, y].number = -10 }
            }
        }
    }

    @Test
    fun testInvalidRange() {
        for (x in listOf(-20, -1, 9, 20)) {
            for (y in listOf(-20, -1, 9, 20)) {
                assertThrows<IndexOutOfBoundsException> { defaultGrid[x, y].number = 5 }
            }
        }
    }

    @DisplayName("Check column validation for different values")
    @ParameterizedTest(name = "{displayName}({arguments})")
    @MethodSource("testColumnValidationCoordinates")
    fun testCheckValidateColumn(x: Int, y1: Int, y2: Int) {
        defaultGrid[x, y1].number = 1
        defaultGrid[x, y2].number = 2
        assertTrue { defaultGrid[x, y1].isValid() }
        assertTrue { defaultGrid[x, y2].isValid() }
        assertTrue { defaultGrid.isValid() }
    }

    @DisplayName("Check column validation for repeated values")
    @ParameterizedTest(name = "{displayName}({arguments})")
    @MethodSource("testColumnValidationCoordinates")
    fun testCheckInvalidColumn(x: Int, y1: Int, y2: Int) {
        defaultGrid[x, y1].number = 2
        defaultGrid[x, y2].number = 2
        assertFalse { defaultGrid[x, y1].isValid() }
        assertFalse { defaultGrid[x, y2].isValid() }
        assertFalse { defaultGrid.isValid() }
    }

    @DisplayName("Check row validation for different values")
    @ParameterizedTest(name = "{displayName}({arguments})")
    @MethodSource("testRowValidationCoordinates")
    fun testCheckValidateRow(x1: Int, x2: Int, y: Int) {
        defaultGrid[x1, y].number = 1
        defaultGrid[x2, y].number = 2
        assertTrue { defaultGrid[x1, y].isValid() }
        assertTrue { defaultGrid[x2, y].isValid() }
        assertTrue { defaultGrid.isValid() }
    }

    @DisplayName("Check row validation for repeated values")
    @ParameterizedTest(name = "{displayName}({arguments})")
    @MethodSource("testRowValidationCoordinates")
    fun testCheckInvalidRow(x1: Int, x2: Int, y: Int) {
        defaultGrid[x1, y].number = 2
        defaultGrid[x2, y].number = 2
        assertFalse { defaultGrid[x1, y].isValid() }
        assertFalse { defaultGrid[x2, y].isValid() }
        assertFalse { defaultGrid.isValid() }
    }

    @DisplayName("Check group validation for different values")
    @ParameterizedTest(name = "{displayName}({arguments})")
    @MethodSource("testGroupValidationCoordinates")
    fun testCheckValidateGroup(x1: Int, y1: Int, x2: Int, y2: Int) {
        defaultGrid[x1, y1].number = 1
        defaultGrid[x2, y2].number = 2
        assertTrue { defaultGrid[x1, y1].isValid() }
        assertTrue { defaultGrid[x2, y2].isValid() }
        assertTrue { defaultGrid.isValid() }
    }

    @DisplayName("Check group validation for repeated values")
    @ParameterizedTest(name = "{displayName}({arguments})")
    @MethodSource("testGroupValidationCoordinates")
    fun testCheckInvalidGroup(x1: Int, y1: Int, x2: Int, y2: Int) {
        defaultGrid[x1, y1].number = 2
        defaultGrid[x2, y2].number = 2
        assertFalse { defaultGrid[x1, y1].isValid() }
        assertFalse { defaultGrid[x2, y2].isValid() }
        assertFalse { defaultGrid.isValid() }
    }

    companion object {
        @JvmStatic
        fun testColumnValidationCoordinates(): List<Arguments> = mutableListOf<Arguments>().apply {
            for (x in 0..8) {
                for (skipGroup in 0..2) {
                    for (group1Y in 0..2) {
                        for (group2Y in 0..2) {
                            when (skipGroup) {
                                0    -> add(arguments(x, 3 + group1Y, 6 + group2Y))
                                1    -> add(arguments(x, group1Y, 6 + group2Y))
                                else -> add(arguments(x, group1Y, 3 + group2Y))
                            }
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun testRowValidationCoordinates(): List<Arguments> = mutableListOf<Arguments>().apply {
            for (y in 0..8) {
                for (skipGroup in 0..2) {
                    for (group1X in 0..2) {
                        for (group2X in 0..2) {
                            when (skipGroup) {
                                0    -> add(arguments(3 + group1X, 6 + group2X, y))
                                1    -> add(arguments(group1X, 6 + group2X, y))
                                else -> add(arguments(group1X, 3 + group2X, y))
                            }
                        }
                    }
                }
            }
        }


        @JvmStatic
        fun testGroupValidationCoordinates(): List<Arguments> = mutableListOf<Arguments>().apply {
            for (groupX in listOf(0, 3, 6)) {
                for (groupY in listOf(0, 3, 6)) {
                    for (i in 0..7) {
                        for (j in (i + 1..8)) {
                            add(
                                arguments(
                                    groupX + i % 3,
                                    groupY + i / 3,
                                    groupX + j % 3,
                                    groupY + j / 3
                                         )
                               )
                        }
                    }
                }
            }
        }
    }
}