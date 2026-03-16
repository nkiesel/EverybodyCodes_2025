import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest03 {
    private fun parse(input: List<String>) = input[0].split(",").map { it.toInt() }

    fun one(input: List<String>): Int {
        return parse(input).toSet().sum()
    }

    fun two(input: List<String>): Int {
        return parse(input).toSet().sorted().take(20).sum()
    }

    fun three(input: List<String>): Int {
        var list = parse(input).sortedDescending()
        var sets = 0
        while (list.isNotEmpty()) {
            sets++
            list = buildList {
                for ((x, y) in list.windowed(2)) {
                    if (x == y) add(x)
                }
            }
        }

        return sets
    }
}

val Quest03Test by testSuite {
    val quest = "03"

    with(Quest03) {
        test("one") {
            val sample = """
                10,5,1,10,3,8,5,2,2
            """.trimIndent().lines()
            one(sample) shouldBe 29

            val input = lines(quest, 1)
            one(input) shouldBe 2568
        }

        test("two") {
            val sample = """
                4,51,13,64,57,51,82,57,16,88,89,48,32,49,49,2,84,65,49,43,9,13,2,3,75,72,63,48,61,14,40,77
            """.trimIndent().lines()
            two(sample) shouldBe 781

            val input = lines(quest, 2)
            two(input) shouldBe 226
        }

        test("three") {
            val sample = """
                4,51,13,64,57,51,82,57,16,88,89,48,32,49,49,2,84,65,49,43,9,13,2,3,75,72,63,48,61,14,40,77
            """.trimIndent().lines()
            three(sample) shouldBe 3

            val input = lines(quest, 3)
            three(input) shouldBe 4481
        }
    }
}
