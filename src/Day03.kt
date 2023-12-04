fun main() {
    val dirs = listOf(
        Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
        Pair(0, -1), Pair(0, 1),
        Pair(1, -1), Pair(1, 0), Pair(1, 1),
    )

    fun findAndAddNumber(currentCol: Int, currentRow: Int, row: CharArray, numberMap: MutableMap<Pair<Int, Int>, Int>):
            Pair<Int, Int> {
        var startCol = currentCol
        var col = currentCol
        col -= 1
        while (col >= 0) {
            if (!row[col].isDigit()) { break }
            startCol = col
            col -= 1
        }

        var endCol = currentCol
        col = currentCol
        col += 1
        while (col < row.size) {
            if (!row[col].isDigit()) { break }
            endCol = col
            col += 1
        }

        val number = String(row.sliceArray(startCol until  endCol + 1)).toInt()
        numberMap[Pair(currentRow, startCol)] = number

//        println("row: ${String(row)}, currentCol: ${currentCol}, number: $number")

        return Pair(currentRow, startCol)
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }
        val rowCount = grid.size
        val colCount = grid.first().size
        val numberMap = mutableMapOf<Pair<Int, Int>, Int>()

        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char != '.' && !char.isDigit()) { // special symbol
                    dirs.forEach { (m, n) ->
                        val (newI, newJ) = Pair(i + m, j + n)
                        if (newI in 0..< rowCount && newJ in 0..< colCount) {
                            if (grid[newI][newJ].isDigit()) {
                                findAndAddNumber(newJ, newI, grid[newI], numberMap)
                            }
                        }
                    }

                }
            }
        }

        return numberMap.values.sum()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }
        val rowCount = grid.size
        val colCount = grid.first().size
        val numberMap = mutableMapOf<Pair<Int, Int>, Int>()
        var sum = 0

        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char == '*') { // gear symbol
                    val foundNumbers = mutableSetOf<Pair<Int, Int>>()

                    dirs.forEach { (m, n) ->
                        val (newI, newJ) = Pair(i + m, j + n)
                        if (newI in 0..< rowCount && newJ in 0..< colCount) {
                            if (grid[newI][newJ].isDigit()) {
                                val pos = findAndAddNumber(newJ, newI, grid[newI], numberMap)
                                foundNumbers.add(pos)
                            }
                        }
                    }
                    if (foundNumbers.size == 2) {
                        sum += numberMap[foundNumbers.first()]!! * numberMap[foundNumbers.last()]!!
                    }
                }
            }
        }

        return sum
    }



    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_sample")
    check(part1(testInput) == 4361)

    val testInput2 = readInput("Day03_sample")
    check(part2(testInput2) == 467835)

    val input = readInput("Day03")
    part1(input).println()
     part2(input).println()
}
