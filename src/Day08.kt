import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        /*
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
         */
        var directions = listOf<String>()
        val map = mutableMapOf<String, Pair<String, String>>()

        input.forEachIndexed { index, line ->
            if (index == 0) {
                directions = line.split("").filter { it.isNotEmpty() }
                return@forEachIndexed
            }
            if (index >= 2) {
                var parts = line.split(" = ")
                var dirs = parts.last().substring(1, parts.last().length - 1).split(", ")
                map[parts.first()] = Pair(dirs.first(), dirs.last())
            }
        }

        var steps = 0
        var dirIndex = 0
        var currentPos = "AAA"
        while (true) {
            val dir = directions[dirIndex]
            val nextOptions = map[currentPos]!!
            val nextPos = if (dir == "R") { nextOptions.second } else { nextOptions.first }
            steps += 1
            if (nextPos == "ZZZ") {
                break
            }
            currentPos = nextPos
            dirIndex += 1
            if (dirIndex == directions.size) {
                dirIndex = 0
            }
        }
        return steps
    }

    fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        return (a * b) / gcd(a, b)
    }

    fun lcmOfArray(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = lcm(result, numbers[i])
        }
        return result
    }

    fun part2(input: List<String>): Long {
        /*
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
         */

        var directions = listOf<String>()
        val map = mutableMapOf<String, Pair<String, String>>()
        val currentPoses = mutableListOf<String>()

        input.forEachIndexed { index, line ->
            if (index == 0) {
                directions = line.split("").filter { it.isNotEmpty() }
                return@forEachIndexed
            }
            if (index >= 2) {
                val parts = line.split(" = ")
                val dirs = parts.last().substring(1, parts.last().length - 1).split(", ")
                val key = parts.first()
                map[key] = Pair(dirs.first(), dirs.last())
                if (key.endsWith("A")) {
                    currentPoses.add(key)
                }
            }
        }

        val stepsArray = currentPoses.map {
            var dirIndex = 0
            var currentPos = it
            var steps = 0L
            while (true) {
                val dir = directions[dirIndex]
                val nextOptions = map[currentPos]!!
                val nextPos = if (dir == "R") { nextOptions.second } else { nextOptions.first }
                steps += 1
                if (nextPos.endsWith("Z")) {
                    break
                }
                currentPos = nextPos
                dirIndex += 1
                if (dirIndex == directions.size) {
                    dirIndex = 0
                }
            }
            steps
        }

        return lcmOfArray(stepsArray)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_sample")
    check(part1(testInput) == 2)

    val testInput1_2 = readInput("Day08_sample1_2")
    check(part1(testInput1_2) == 6)

    val testInput2 = readInput("Day08_sample2")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
