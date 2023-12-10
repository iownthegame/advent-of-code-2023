import java.util.*
import kotlinx.coroutines.*

fun main() {
    /*
    seeds: 79 14 55 13

    seed-to-soil map:
    50 98 2
    52 50 48

    soil-to-fertilizer map:
    0 15 37
    37 52 2
    39 0 15

    fertilizer-to-water map:
    49 53 8
    0 11 42
    42 0 7
    57 7 4

    water-to-light map:
    88 18 7
    18 25 70

    light-to-temperature map:
    45 77 23
    81 45 19
    68 64 13

    temperature-to-humidity map:
    0 69 1
    1 0 69

    humidity-to-location map:
    60 56 37
    56 93 4
     */

    fun parseInput(input: List<String>, map: MutableMap<String, MutableList<List<Long>>>, mapNames: MutableList<String>): List<Long> {
        var seeds = listOf<Long>()
        var mapName: String? = null

        input.forEach { line ->
            if (line.isEmpty()) {
                mapName = null
                return@forEach
            }

            if (mapName != null) {
                // keep parsing map
                // 45 77 23
                // destinationRangeStart, sourceRangeStart, rangeLength
                map[mapName!!]!!.add(line.split(" ").map { it.trim().toLong() })
            }

            if (line.contains("map")) {
                mapName = line.split(" ").first()
                mapNames.add(mapName!!)
                map[mapName!!] = mutableListOf()
            }

            if (line.contains("seeds")) {
                seeds = line.split(": ").last().split(" ").map { it.trim().toLong() }
                return@forEach
            }
        }

        return seeds
    }

    suspend fun getLocations(seeds: List<Long>, map: MutableMap<String, MutableList<List<Long>>>, mapNames: MutableList<String> ): List<Long> {
        val result = withContext(Dispatchers.Default) {
            val deferredResults = seeds.map { seed ->
                async {
//                    println("get location for seed $seed")
                    var source: Long
                    var destination = seed

                    mapNames.forEach { mapName ->
                        source = destination

                        for (rangeList in map[mapName]!!) {
                            val (destinationRangeStart, sourceRangeStart, rangeLength) = rangeList
                            if (source >= sourceRangeStart && source <= sourceRangeStart + rangeLength - 1) {
                                destination = source - sourceRangeStart + destinationRangeStart
                                break
                            }
                        }
                    }
                    destination
                }
            }
            deferredResults.awaitAll()
        }
        return result
    }

    fun part1(input: List<String>): Long {
        val map = mutableMapOf<String, MutableList<List<Long>>>()
        val mapNames = mutableListOf<String>()

        val seeds = parseInput(input, map, mapNames)
        val locations = runBlocking {  getLocations(seeds, map, mapNames) }

        return locations.minOfOrNull { it }!!
    }

    fun part2(input: List<String>): Long {
        val map = mutableMapOf<String, MutableList<List<Long>>>()
        val mapNames = mutableListOf<String>()

        val seeds = parseInput(input, map, mapNames)
        val newSeeds = mutableListOf<Long>()
        seeds.chunked(seeds.size / 2) { chunk ->
            val (seed, range) = chunk
            for (i in 0 until range) {
                newSeeds.add(seed + i)
            }
        }
        val locations = runBlocking {  getLocations(newSeeds, map, mapNames) }

        return locations.minOfOrNull { it }!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_sample")
    check(part1(testInput) == 35L)

    val testInput2 = readInput("Day05_sample")
    check(part2(testInput2) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
