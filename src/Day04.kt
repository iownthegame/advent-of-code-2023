import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        var sum = 0

        input.forEach { line ->
            val (answerString, cardString) = line.split(" | ")
            val myNumbers = cardString.split(" ").filter { !it.isEmpty() }.map { it.trim().toInt() }
            val winningNumbers = answerString.split(": ").last().split(" ").filter { !it.isEmpty() }.map { it.trim().toInt() }
            var score = 0
            myNumbers.forEach {
                if (winningNumbers.contains(it)) {
                    if (score == 0) {
                        score = 1
                    } else {
                        score *= 2
                    }
                }
            }
            sum += score
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val matches = mutableListOf<Int>()

        input.forEach { line ->
            val (answerString, cardString) = line.split(" | ")
            val myNumbers = cardString.split(" ").filter { !it.isEmpty() }.map { it.trim().toInt() }
            val winningNumbers = answerString.split(": ").last().split(" ").filter { !it.isEmpty() }.map { it.trim().toInt() }
            val match = myNumbers.count { winningNumbers.contains(it) }
            matches.add(match)
        }

        val cards: Queue<Int> = LinkedList()
        input.forEachIndexed { index, _ -> cards.add(index)}

        var result = cards.size
        while (!cards.isEmpty()) {
            val currentCard = cards.poll()
            result += matches[currentCard]
            for (i in 0..<matches[currentCard]) {
                cards.add(currentCard + i + 1)
            }
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_sample")
    check(part1(testInput) == 13)

    val testInput2 = readInput("Day04_sample")
    check(part2(testInput2) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
