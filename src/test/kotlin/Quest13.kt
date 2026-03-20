import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest13 {
    private fun parse(input: List<String>) = input.map { it.toInt() }

    fun one(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        var nl = false
        input.forEach { (if (nl) left else right) += it.toInt(); nl = !nl }
        val nums = listOf(1) + right + left.reversed()
        return nums[2025 % nums.size]
    }

    fun two(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        var nl = false
        input.map { it.ints(false).let { it[0]..it[1]} }.forEach { r ->
            val l = (if (nl) left else right)
            r.forEach { l += it }
            nl = !nl
        }
        val nums = listOf(1) + right + left.reversed()
        return nums[20252025 % nums.size]
    }

    fun three(input: List<String>): Long {
        val left = mutableListOf<Long>()
        val right = mutableListOf<Long>()
        var nl = false
        input.map { it.longs(false).let { it[0]..it[1]} }.forEach { r ->
            val l = (if (nl) left else right)
            r.forEach { l += it }
            nl = !nl
        }
        val nums = listOf(1L) + right + left.reversed()
        return nums[(202520252025L % nums.size.toLong()).toInt()]
    }
}

val Quest13Test by testSuite {
    val quest = "13"

    with(Quest13) {
        test("one") {
            val sample = """
                72
                58
                47
                61
                67
            """.trimIndent().lines()
            one(sample) shouldBe 67

            val input = lines(quest, 1)
            one(input) shouldBe 252
        }

        test("two") {
            val sample = """
                10-15
                12-13
                20-21
                19-23
                30-37
            """.trimIndent().lines()
            two(sample) shouldBe 30

            val input = lines(quest, 2)
            two(input) shouldBe 9948
        }

        test("three") {
            val input = lines(quest, 3)
            three(input) shouldBe 221146L
        }
    }
}
