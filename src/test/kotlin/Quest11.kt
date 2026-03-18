import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.abs

object Quest11 {
    private fun parse(input: List<String>) = input.map { it.toInt() }.toIntArray()

    fun one(input: List<String>): Int {
        val ducks = parse(input)
        var phase = 1
        var round = 0
        while (round < 10) {
            var moved = false
            for (i in 0 until ducks.lastIndex) {
                val a = ducks[i]
                val b = ducks[i + 1]
                if (phase == 1 && b < a) {
                    ducks[i]--
                    ducks[i + 1]++
                    moved = true
                } else if (phase == 2 && b > a) {
                    ducks[i]++
                    ducks[i + 1]--
                    moved = true
                }
            }
            when {
                moved -> round++
                phase == 1 -> phase = 2
                else -> break
            }
        }
        return ducks.withIndex().sumOf { (i, v) -> (i + 1) * v }
    }

    fun two(input: List<String>): Int {
        val ducks = parse(input)
        var phase = 1
        var round = 1
        while (true) {
            var moved = false
            for (i in 0 until ducks.lastIndex) {
                val a = ducks[i]
                val b = ducks[i + 1]
                if (phase == 1 && b < a) {
                    ducks[i]--
                    ducks[i + 1]++
                    moved = true
                } else if (phase == 2 && b > a) {
                    ducks[i]++
                    ducks[i + 1]--
                    moved = true
                }
            }
            when {
                ducks.all { it == ducks[0] } -> return round
                moved -> round++
                phase == 1 -> phase = 2
                else -> error("no solution")
            }
        }
    }

    fun three(input: List<String>): Long {
        val ducks = input.map { it.toLong() }.toLongArray()
        var round = 0L
        do {
            var moved = false
            for (i in 0 until ducks.lastIndex) {
                val a = ducks[i]
                val b = ducks[i + 1]
                if (b < a) {
                    ducks[i]--
                    ducks[i + 1]++
                    moved = true
                }
            }
            round++
        } while (moved)
        val avg = ducks.sum() / ducks.size
        return ducks.toList().sumOf { abs(it - avg) } / 2 + round - 1
    }
}

val Quest11Test by testSuite {
    val quest = "11"

    with(Quest11) {
        test("one") {
            val sample = """
                9
                1
                1
                4
                9
                6
            """.trimIndent().lines()
            one(sample) shouldBe 109

            val input = lines(quest, 1)
            one(input) shouldBe 252
        }

        test("two") {
            val sample1 = """
                9
                1
                1
                4
                9
                6
            """.trimIndent().lines()
            two(sample1) shouldBe 11
            three(sample1) shouldBe 11L

            val sample2 = """
                805
                706
                179
                48
                158
                150
                232
                885
                598
                524
                423
            """.trimIndent().lines()
            two(sample2) shouldBe 1579
            three(sample2) shouldBe 1579L

            val input = lines(quest, 2)
            two(input) shouldBe 3586398
            three(input) shouldBe 3586398L
        }

        test("three") {
            val input = lines(quest, 3)
            three(input) shouldBe 129856063131908L
        }
    }
}
