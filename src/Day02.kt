fun main() {
    fun isPossibleAnswer(bag: MutableMap<String, Int>): Boolean {
        // 12 red cubes, 13 green cubes, and 14 blue cubes
        if (bag.getOrDefault("red", 0) > 12) return false
        if (bag.getOrDefault("green", 0) > 13) return false
        if (bag.getOrDefault("blue", 0) > 14) return false
        return true
    }

    fun part1(input: List<String>): Int {
        // Sample input string
        // Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        return input.sumOf { line ->
            val (gameString, bagsString) = line.split(": ").map { it.trim() }
            val gameIndex = gameString.split(" ").last().toInt()
            val bags = bagsString.split("; ").map { // bagString
                val bag = mutableMapOf<String, Int>()
                it.split(", ").forEach { colorString ->
                    val (amount, color) = colorString.split(" ").map { it.trim() }
                    bag[color] = amount.toInt()
                }
                bag
            }

            if (bags.all { isPossibleAnswer(it) }) gameIndex else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val (_, bagsString) = line.split(": ").map { it.trim() }
            val fewestCubeInBag = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            bagsString.split("; ").map { // bagString
                it.split(", ").forEach { colorString ->
                    val (amount, color) = colorString.split(" ").map { it.trim() }
                    fewestCubeInBag[color] = maxOf(fewestCubeInBag[color]!!, amount.toInt())
                }
            }

            fewestCubeInBag["red"]!! * fewestCubeInBag["green"]!! * fewestCubeInBag["blue"]!!
        }
    }



    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_sample")
    check(part1(testInput) == 8)

     val testInput2 = readInput("Day02_sample")
     check(part2(testInput2) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
