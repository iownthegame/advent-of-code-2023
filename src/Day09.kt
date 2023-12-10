fun main() {
    fun calculatePredict(numbers: List<Int>): Int {
        /* 0 3 6 9 12 15 */
        var reducedNumbers = numbers
        var sumOfLastNumbers = numbers.last()
        while (true) {
            val newNumbers = mutableListOf<Int>()
            reducedNumbers.forEachIndexed { index, _ ->
                if (index > 0) {
                    newNumbers.add(reducedNumbers[index] - reducedNumbers[index - 1])
                }
            }
            if (newNumbers.all { it == 0 }) {
                break
            }
            sumOfLastNumbers += newNumbers.last()
            reducedNumbers = newNumbers
        }
        return sumOfLastNumbers
    }

    fun calculatePredictPart2(numbers: List<Int>): Int {
        /* 10  13  16  21  30  45 */
        var reducedNumbers = numbers
        var firstNumbers = mutableListOf(numbers.first())
        while (true) {
            val newNumbers = mutableListOf<Int>()
            reducedNumbers.forEachIndexed { index, _ ->
                if (index > 0) {
                    newNumbers.add(reducedNumbers[index] - reducedNumbers[index - 1])
                }
            }
            if (newNumbers.all { it == 0 }) {
                break
            }
            firstNumbers.add(newNumbers.first())
            reducedNumbers = newNumbers
        }

        // [10, 3, 0, 2] -> [2, 0, 3, 10]
        firstNumbers.reverse()
        var result = 0
        firstNumbers.forEach {
            result = (result - it) * (-1)
        }
        return result
    }

    fun part1(input: List<String>): Int {
        /*
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
        */
        val predicts = input.map { line ->
            val numbers = line.split(" ").map { it.trim().toInt() }
            calculatePredict(numbers)
        }
        return predicts.sum()
    }

    fun part2(input: List<String>): Int {
        val predicts = input.map { line ->
            val numbers = line.split(" ").map { it.trim().toInt() }
            calculatePredictPart2(numbers)
        }
        return predicts.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_sample")
    check(part1(testInput) == 114)

    val testInput2 = readInput("Day09_sample")
    check(part2(testInput2) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
