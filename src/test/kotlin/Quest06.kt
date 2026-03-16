import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.max
import kotlin.math.min

object Quest06 {
    fun one(input: List<String>): Int {
        var r = 0
        var nA = 0
        input[0].forEach { c ->
            when (c) {
                'A' -> nA++
                'a' -> r += nA
            }
        }
        return r
    }

    fun two(input: List<String>): Int {
        var r = 0
        var nA = 0
        var nB = 0
        var nC = 0
        input[0].forEach { c ->
            when (c) {
                'A' -> nA++
                'B' -> nB++
                'C' -> nC++
                'a' -> r += nA
                'b' -> r += nB
                'c' -> r += nC
            }
        }
        return r
    }

    fun three(input: List<String>, rep: Int = 1000, dist: Int = 1000): Int {
        val t = input[0].repeat(rep)
        return t.withIndex().sumOf { (i, c) ->
            if (c.isLowerCase()) {
                val s = t.substring(max(0, i - dist), min(t.length, i + dist + 1))
                when (c) {
                    'a' -> s.count { it == 'A' }
                    'b' -> s.count { it == 'B' }
                    'c' -> s.count { it == 'C' }
                    else -> 0
                }
            } else {
                0
            }
        }
    }
}

val Quest06Test by testSuite {
    val quest = "06"

    with(Quest06) {
        test("one") {
            val sample = """
                ABabACacBCbca
            """.trimIndent().lines()
            one(sample) shouldBe 5

            val input = lines(quest, 1)
            one(input) shouldBe 152
        }

        test("two") {
            val sample = """
                ABabACacBCbca
            """.trimIndent().lines()
            two(sample) shouldBe 11

            val input = lines(quest, 2)
            two(input) shouldBe 3613
        }

        test("three") {
            val sample = """
                AABCBABCABCabcabcABCCBAACBCa
            """.trimIndent().lines()
            three(sample, 1, 10) shouldBe 34
            three(sample, 2, 10) shouldBe 72
            three(sample) shouldBe 3442321

            val input = lines(quest, 3)
            three(input) shouldBe 1666116348
        }
    }
}
