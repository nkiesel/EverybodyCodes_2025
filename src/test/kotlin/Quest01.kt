import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest01 {
    private fun parse(input: List<String>): Pair<List<String>, List<Int>> {
        val dir = mapOf('L' to -1, 'R' to 1)
        val names = input[0].split(",")
        val instructions = input[2].split(",").map { dir[it.first()]!! * it.drop(1).toInt() }
        return Pair(names, instructions)
    }

    fun one(input: List<String>): String {
        val (names, instructions) = parse(input)
        val last = names.lastIndex
        var pos = 0
        for (i in instructions) {
            pos += i
            pos = when {
                pos < 0 -> 0
                pos > last -> last
                else -> pos
            }
        }
        return names[pos]
    }

    fun two(input: List<String>): String {
        val (names, instructions) = parse(input)
        val size = names.size
        var pos = 0
        for (i in instructions) {
            pos = ((pos + i) % size + size) % size
        }
        return names[pos]
    }

    fun three(input: List<String>): String {
        val (n, instructions) = parse(input)
        val names = n.toMutableList()
        val size = names.size
        for (i in instructions) {
            val next = (i % size + size) % size
            names[0] = names[next].also { names[next] = names[0] }
        }
        return names[0]
    }
}

val Quest01Test by testSuite {
    val quest = "01"

    with(Quest01) {
        test("one") {
            val sample = """
                Vyrdax,Drakzyph,Fyrryn,Elarzris

                R3,L2,R3,L1
            """.trimIndent().lines()
            one(sample) shouldBe "Fyrryn"

            val input = lines(quest, 1)
            one(input) shouldBe "Ulkagrath"
        }

        test("two") {
            val sample = """
                Vyrdax,Drakzyph,Fyrryn,Elarzris

                R3,L2,R3,L1
            """.trimIndent().lines()
            two(sample) shouldBe "Elarzris"
            val input = lines(quest, 2)
            two(input) shouldBe "Wynnoris"
        }

        test("three") {
            val sample3 = """
                Vyrdax,Drakzyph,Fyrryn,Elarzris

                R3,L2,R3,L3
            """.trimIndent().lines()
            three(sample3) shouldBe "Drakzyph"

            val input = lines(quest, 3)
            three(input) shouldBe "Ulmarloris"
        }
    }
}
