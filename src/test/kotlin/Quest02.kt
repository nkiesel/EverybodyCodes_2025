import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest02 {
    data class Complex(val x: Long, val y: Long) {
        operator fun plus(other: Complex) = Complex(x + other.x, y + other.y)
        operator fun minus(other: Complex) = Complex(x - other.x, y - other.y)
        operator fun times(other: Complex) = Complex(x * other.x - y * other.y, x * other.y + y * other.x)
        operator fun div(other: Complex) = Complex(x / other.x, y / other.y)
        override fun toString() = "[$x,$y]"
        fun inRange() = x in valid && y in valid

        companion object {
            val valid = -1000000..1000000
        }
    }

    private fun parse(input: List<String>) = input[0].longs().let { Complex(it[0], it[1]) }

    fun one(input: List<String>): String {
        val a = parse(input)
        var r = Complex(0, 0)
        val ten = Complex(10, 10)
        repeat(3) {
            r = r * r / ten + a
        }
        return r.toString()
    }

    fun three(input: List<String>, steps: Long = 1L): Int {
        val a = parse(input)
        val ht = Complex(100000, 100000)
        var engraved = 0
        for (y in a.y..(a.y + 1000) step steps) {
            loop@ for (x in a.x..(a.x + 1000) step steps) {
                val c = Complex(x, y)
                var r = Complex(0, 0)
                repeat(100) {
                    r = r * r / ht + c
                    if (!r.inRange()) continue@loop
                }
                engraved++
            }
        }
        return engraved
    }
}

val Quest02Test by testSuite {
    val quest = "02"

    with(Quest02) {
        test("one") {
            val sample = """
                A=[25,9]
            """.trimIndent().lines()
            one(sample) shouldBe "[357,862]"

            val input = lines(quest, 1)
            one(input) shouldBe "[217914,979563]"
        }

        test("two") {
            val sample = """
                A=[35300,-64910]
            """.trimIndent().lines()
            three(sample, 10) shouldBe 4076

            val input = lines(quest, 2)
            three(input, 10) shouldBe 1254
        }

        test("three") {
            val sample = """
                A=[35300,-64910]
            """.trimIndent().lines()
            three(sample) shouldBe 406954

            val input = lines(quest, 3)
            three(input) shouldBe 117839
        }

    }
}
