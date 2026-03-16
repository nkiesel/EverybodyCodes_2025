import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object QuestXX {
    private fun parse(input: List<String>) = input

    fun one(input: List<String>): String {
        return ""
    }

    fun two(input: List<String>): String {
        return ""
    }

    fun three(input: List<String>): String {
        return ""
    }
}

val QuestXXTest by testSuite {
    val quest = "XX"

    with(QuestXX) {
        test("one") {
            val sample = """
            """.trimIndent().lines()
            one(sample) shouldBe ""

//            val input = lines(quest, 1)
//            one(input) shouldBe ""
        }

        test("two") {
            val sample = """
            """.trimIndent().lines()
            two(sample) shouldBe ""

//            val input = lines(quest, 2)
//            two(input) shouldBe ""
        }

        test("three") {
            val sample = """
            """.trimIndent().lines()
            three(sample) shouldBe ""

//            val input = lines(quest, 3)
//            three(input) shouldBe ""
        }
    }
}
