import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.abs
import kotlin.math.max

object Quest08 {
    private fun parse(input: List<String>) = input[0].ints()

    fun one(input: List<String>, nails: Int = 32): Int {
        return parse(input).zipWithNext().count { (a, b) -> abs(a - b) == nails / 2 }
    }

    private fun List<IntRange>.intersects(a: Int, b: Int) =
        count { r -> a != r.first && a != r.last && b != r.first && b != r.last && (a in r && b !in r || b in r && a !in r) }

    fun two(input: List<String>): Int {
        var knots = 0
        val threads = mutableListOf<IntRange>()
        parse(input).zipWithNext().forEach { (a, b) ->
            knots += threads.intersects(a, b)
            threads.add(minOf(a, b)..maxOf(a, b))
        }
        return knots
    }

    fun three(input: List<String>, nc: Int = 256): Int {
        val threads = parse(input).zipWithNext().map { (a, b) -> minOf(a, b)..maxOf(a, b) }
        var cuts = 0
        for (a in 1..(nc - 2)) {
            for (b in (a + 2)..nc) {
                cuts = max(cuts, threads.count { it == a..b } + threads.intersects(a, b))
            }
        }
        return cuts
    }
}

val Quest08Test by testSuite {
    val quest = "08"

    with(Quest08) {
        test("one") {
            val sample = """
                1,5,2,6,8,4,1,7,3
            """.trimIndent().lines()
            one(sample, 8) shouldBe 4

            val input = lines(quest, 1)
            one(input) shouldBe 62
        }

        test("two") {
            val sample = """
                1,5,2,6,8,4,1,7,3,5,7,8,2
            """.trimIndent().lines()
            two(sample) shouldBe 21

            val input = lines(quest, 2)
            two(input) shouldBe 2923267
        }

        test("three") {
            val sample = """
                1,5,2,6,8,4,1,7,3,6
            """.trimIndent().lines()
            three(sample, 8) shouldBe 7

            val input = lines(quest, 3)
            three(input) shouldBe 2791
        }
    }
}
