import java.util.*
import kotlinx.coroutines.*


data class Card(
    val card: String,
    val type: CardType,
    val bid: Long,
)

enum class CardType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

fun main() {

    fun detectCardType(card: String): CardType {
        /*
        Five of a kind, where all five cards have the same label: AAAAA
        Four of a kind, where four cards have the same label and one card has a different label: AA8AA
        Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
        Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
        Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
        One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
        High card, where all cards' labels are distinct: 23456
         */
        val cardMap = card.groupingBy { it }.eachCount()
        if (cardMap.keys.size == 1) {
            return CardType.FIVE_OF_A_KIND
        }
        if (cardMap.keys.size == 2) {
            if (cardMap.values.any { it == 4 }) {
                return CardType.FOUR_OF_A_KIND
            }
            if (cardMap.values.any { it == 3 }) {
                return CardType.FULL_HOUSE
            }
        }

        if (cardMap.keys.size == 3) {
            if (cardMap.values.any { it == 3 }) {
                return CardType.THREE_OF_A_KIND
            }
            if (cardMap.values.any { it == 2 }) {
                return CardType.TWO_PAIR
            }
        }

        if (cardMap.keys.size == 4) {
            return CardType.ONE_PAIR
        }

        return CardType.HIGH_CARD
    }

    fun detectCardTypePart2(card: String): CardType {
        val cardMap = card.groupingBy { it }.eachCount()
        if (cardMap.keys.size == 1) {
            return CardType.FIVE_OF_A_KIND
        }
        if (cardMap.keys.size == 2) {
            if (cardMap.values.any { it == 4 }) {  // [4, 1]
                if (cardMap['J'] == 4) { // JJJJ1 -> [5, 0]
                    return CardType.FIVE_OF_A_KIND // upgrade
                }
                if (cardMap['J'] == 1) { // -> [5, 0]
                    return CardType.FIVE_OF_A_KIND // upgrade
                }
                return CardType.FOUR_OF_A_KIND
            }
            if (cardMap.values.any { it == 3 }) { // [3, 2]
                if (cardMap['J'] == 3) { // JJJ11 -> [5, 0]
                    return CardType.FIVE_OF_A_KIND // upgrade
                }
                if (cardMap['J'] == 2) { // -> [5, 0]
                    return CardType.FIVE_OF_A_KIND // upgrade
                }
                return CardType.FULL_HOUSE
            }
        }

        if (cardMap.keys.size == 3) {
            if (cardMap.values.any { it == 3 }) { // [3, 1, 1]
                if (cardMap['J'] == 3) { // JJJ12 -> [4, 1]
                    return CardType.FOUR_OF_A_KIND // upgrade
                }
                if (cardMap['J'] == 1) { // -> [4, 1]
                    return CardType.FOUR_OF_A_KIND // upgrade
                }
                return CardType.THREE_OF_A_KIND
            }
            if (cardMap.values.any { it == 2 }) { // [2, 2, 1]
                if (cardMap['J'] == 2) { // -> [4, 1]
                    return CardType.FOUR_OF_A_KIND // upgrade
                }
                if (cardMap['J'] == 1) { // -> [3, 2]
                    return CardType.FULL_HOUSE // upgrade
                }
                return CardType.TWO_PAIR
            }
        }

        if (cardMap.keys.size == 4) { // [2, 1, 1, 1]
            if (cardMap['J'] == 2) { // 2QJ8J -> [3, 1, 1]
                return CardType.THREE_OF_A_KIND // upgrade
            }
            if (cardMap['J'] == 1) { // -> [3, 1, 1]
                return CardType.THREE_OF_A_KIND // upgrade
            }
            return CardType.ONE_PAIR
        }

        // [1, 1, 1, 1, 1]
        if (cardMap['J'] == 1) { // -> [2, 1, 1, 1]
            return CardType.ONE_PAIR // upgrade
        }
        return CardType.HIGH_CARD
    }

    fun compareCardStrings(cardStr1: String, cardStr2: String, cardRanks: String): Int {
        val rankMap = mutableMapOf<Char, Int>()
        cardRanks.forEachIndexed { index, rank -> rankMap[rank] = index }

        for (i in cardStr1.indices) {
            val rankValue1 = rankMap[cardStr1[i]]!!
            val rankValue2 = rankMap[cardStr2[i]]!!

            if (rankValue1 != rankValue2) {
                return rankValue1.compareTo(rankValue2)
            }
        }

        return 0
    }

    fun processCards(input: List<String>, detectCardTypeFuc: (String) -> CardType): List<Card> =
        /*
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
         */

        input.map { line ->
            val parts = line.split(" ")
            Pair(parts.first(), parts.last().toInt())
            Card(
                card = parts.first(),
                bid = parts.last().toLong(),
                type = detectCardTypeFuc(parts.first()),
            )
        }

    fun calculateScore(cards: List<Card>)
            = cards.mapIndexed { index, card ->  card.bid * (index + 1) }.sum()

    fun part1(input: List<String>): Long {
        val cards = processCards(input, ::detectCardType)
        /* A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2 */
        val cardRanks = "23456789TJQKA"

        val cardComparator = Comparator<Card> { card1, card2 ->
            when {
                (card1.type.ordinal < card2.type.ordinal) -> -1
                (card1.type.ordinal > card2.type.ordinal) -> 1
                /* A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2 */
                else -> compareCardStrings(card1.card, card2.card, cardRanks)
            }
        }
//        println("sorted cards: ${cards.sortedWith(cardComparator)}")
        return calculateScore(cards.sortedWith(cardComparator))
    }

    fun part2(input: List<String>): Long {
        val cards = processCards(input, ::detectCardTypePart2)
        val cardRanks = "J23456789TQKA"

        val cardComparator = Comparator<Card> { card1, card2 ->
            when {
                (card1.type.ordinal < card2.type.ordinal) -> -1
                (card1.type.ordinal > card2.type.ordinal) -> 1
                else -> compareCardStrings(card1.card, card2.card, cardRanks)
            }
        }

//        println("sorted cards: ${cards.sortedWith(cardComparator)}")
        return calculateScore(cards.sortedWith(cardComparator))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_sample")
    check(part1(testInput) == 6440L)

    val testInput2 = readInput("Day07_sample")
    check(part2(testInput2) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
