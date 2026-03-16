import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.ceil

object Quest04 {
    private fun parse(input: List<String>) = input.map { it.toInt() }

    fun one(input: List<String>): Int {
        val w = parse(input)
        return 2025 * w.first() / w.last()
    }

    fun two(input: List<String>): Long {
        val w = parse(input)
        return ceil(w.last().toDouble() * 10000000000000L / w.first()).toLong()
    }

    fun three(input: List<String>): Long {
        var r = 100.0 * input[0].toInt()
        for (l in input.drop(1)) {
            val n = l.ints()
            r /= n[0]
            if (n.size == 2) {
                r *= n[1]
            }
        }
        return r.toLong()
    }
}

val Quest04Test by testSuite {
    val quest = "04"

    with(Quest04) {
        test("one") {
            val sample = """
                102
                75
                50
                35
                13
            """.trimIndent().lines()
            one(sample) shouldBe 15888

            val input = lines(quest, 1)
            one(input) shouldBe 17307
        }

        test("two") {
            val sample = """
                102
                75
                50
                35
                13
            """.trimIndent().lines()
            two(sample) shouldBe 1274509803922L

            val input = lines(quest, 2)
            two(input) shouldBe 2259036144579L
        }

        test("three") {
            val sample = """
                5
                7|21
                18|36
                27|27
                10|50
                10|50
                11
            """.trimIndent().lines()
            three(sample) shouldBe 6818L

            val input = lines(quest, 3)
            three(input) shouldBe 110538673446L
        }
    }
}
