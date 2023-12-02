fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach { line ->
            val chars = line.toCharArray().filter { it.isDigit() }
            sum += "${chars.first()}${chars.last()}".toInt()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val digitLetters = mapOf(
            "one" to '1',
            "two" to '2',
            "three" to '3',
            "four" to '4',
            "five" to '5',
            "six" to '6',
            "seven" to '7',
            "eight" to '8',
            "nine" to '9')
        var sum = 0
        input.forEach { line ->
            val chars = line.toCharArray()
            val newChars = mutableListOf<Char>()
            chars.forEachIndexed { index, c ->
                if (c.isDigit()) {
                    newChars.add(c)
                } else {
                    if (index + 2 < chars.size) {
                        val digitLetter = chars.concatToString(index)
                        digitLetters.keys.forEach mapKey@{
                            if (digitLetter.startsWith(it)) {
                                newChars.add(digitLetters[it]!!)
                                return@mapKey
                            }
                        }
                    }
                }
            }
            sum += "${newChars.first()}${newChars.last()}".toInt()
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_sample")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_sample2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
