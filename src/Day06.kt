import java.util.*
import kotlinx.coroutines.*

fun main() {
    /*
    Time:      7  15   30
    Distance:  9  40  200
     */

    fun calculateWays(time: Long, distance: Long): Long {
        var ways = 0L
        for (t in 1.. time) {
            val race = (time - t) * t
            if (race > distance) {
//                println("time: $t, race: $race")
                ways += 1
            }
        }
        return ways
    }

    fun part1(input: List<String>): Long {
        val times = input[0].split(":").last().split("  ").filter { it.isNotEmpty() }. map { it.trim().toLong() }
        val distances = input[1].split(":").last().split("  ").filter { it.isNotEmpty() }.map { it.trim().toLong() }
        var result = 1L
        val ways = times.mapIndexed { index, time ->
            val way = calculateWays(time, distances[index])
            println("time: $time, dis: ${distances[index]}, way: $way")
            result *= way
        }

        return result
    }

    fun part2(input: List<String>): Long {
        val time = input[0].split(":").last().split("  ").filter { it.isNotEmpty() }.joinToString("") { it.trim() }.toLong()
        val distance = input[1].split(":").last().split("  ").filter { it.isNotEmpty() }.joinToString("") { it.trim() }.toLong()
        return calculateWays(time, distance)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_sample")
    check(part1(testInput) == 288L)

    val testInput2 = readInput("Day06_sample")
    check(part2(testInput2) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
