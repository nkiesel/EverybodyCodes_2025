import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest05 {
    data class Spine(val c: Int) {
        var l: Int? = null
        var r: Int? = null

        fun add(n: Int): Boolean {
            when {
                l == null && n < c -> l = n
                r == null && n > c -> r = n
                else -> return false
            }
            return true
        }

        val number by lazy { ((l?.toString() ?: "") + c.toString() + (r?.toString() ?: "")).toLong() }
    }

    class Sword(input: String) {
        val id = input.substringBefore(':').toInt()
        val spines = sword(input)
        val quality = spines.map { it.c }.joinToString("").toLong()

        private fun sword(input: String): List<Spine> {
            val nums = input.ints().drop(1)
            val spines = mutableListOf<Spine>()
            var spine: Spine? = null
            for (n in nums) {
                if (spine == null || !spines.any { it.add(n) }) {
                    spine = Spine(n)
                    spines.add(spine)
                }
            }
            return spines
        }
    }

    private fun parse(input: List<String>): List<Sword> = input.map { Sword(it) }

    fun one(input: List<String>): Long {
        return parse(input).first().quality
    }

    fun two(input: List<String>): Long {
        val qualities = parse(input).map { it.quality }
        return qualities.max() - qualities.min()
    }

    fun three(input: List<String>): Int {
        val sorted = parse(input).sortedWith(
            compareBy<Sword> { it.quality }.thenComparator { a, b ->
                for (pair in a.spines.zip(b.spines)) {
                    if (pair.first.number < pair.second.number) return@thenComparator -1
                    if (pair.first.number > pair.second.number) return@thenComparator 1
                }
                if (a.spines.size < b.spines.size) return@thenComparator -1
                if (a.spines.size > b.spines.size) return@thenComparator 1
                a.id - b.id
            }
        )
        return sorted.reversed().withIndex().sumOf { (i, v) -> (i + 1) * v.id }
    }
}

val Quest05Test by testSuite {
    val quest = "05"

    with(Quest05) {
        test("one") {
            val sample = """
                58:5,3,7,8,9,10,4,5,7,8,8
            """.trimIndent().lines()
            one(sample) shouldBe 581078L

            val input = lines(quest, 1)
            one(input) shouldBe 5754378535L
        }

        test("two") {
            val sample = """
                1:2,4,1,1,8,2,7,9,8,6
                2:7,9,9,3,8,3,8,8,6,8
                3:4,7,6,9,1,8,3,7,2,2
                4:6,4,2,1,7,4,5,5,5,8
                5:2,9,3,8,3,9,5,2,1,4
                6:2,4,9,6,7,4,1,7,6,8
                7:2,3,7,6,2,2,4,1,4,2
                8:5,1,5,6,8,3,1,8,3,9
                9:5,7,7,3,7,2,3,8,6,7
                10:4,1,9,3,8,5,4,3,5,5
            """.trimIndent().lines()
            two(sample) shouldBe 77053L

            val input = lines(quest, 2)
            two(input) shouldBe 8472967122803L
        }

        test("three") {
            val sample = """
                1:7,1,9,1,6,9,8,3,7,2
                2:6,1,9,2,9,8,8,4,3,1
                3:7,1,9,1,6,9,8,3,8,3
                4:6,1,9,2,8,8,8,4,3,1
                5:7,1,9,1,6,9,8,3,7,3
                6:6,1,9,2,8,8,8,4,3,5
                7:3,7,2,2,7,4,4,6,3,1
                8:3,7,2,2,7,4,4,6,3,7
                9:3,7,2,2,7,4,1,6,3,7
            """.trimIndent().lines()
            three(sample) shouldBe 260

            val input = lines(quest, 3)
            three(input) shouldBe 31373112
        }
    }
}
