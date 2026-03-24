import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest17 {
    private fun parse(input: List<String>) = CharArea(input)

    fun one(input: List<String>): Int {
        val R = 10
        var sum = 0
        val area = parse(input)
        val volcano = area.first('@')
        area.tiles { it != '@' }.forEach { tile ->
            val x = (volcano.x - tile.x)
            val y = (volcano.y - tile.y)
            if (x * x + y * y <= R * R) sum += area[tile].digitToInt()
        }
        return sum
    }

    fun two(input: List<String>): Int {
        val area = parse(input)
        val volcano = area.first('@')
        var max = 0
        var result = 0
        var r = 0
        do {
            r++
            var sum = 0
            area.tiles { it.isDigit() }.forEach {
                val d = volcano - it
                if (d.x * d.x + d.y * d.y <= r * r) {
                    sum += area[it].digitToInt()
                    area[it] = '.'
                }
            }
            if (sum > max) {
                max = sum
                result = r * sum
            }
        } while (sum > 0)
        return result
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest17Test by testSuite {
    val quest = "17"

    with(Quest17) {
        test("one") {
            val sample = """
                189482189843433862719
                279415473483436249988
                432746714658787816631
                428219317375373724944
                938163982835287292238
                627369424372196193484
                539825864246487765271
                517475755641128575965
                685934212385479112825
                815992793826881115341
                1737798467@7983146242
                867597735651751839244
                868364647534879928345
                519348954366296559425
                134425275832833829382
                764324337429656245499
                654662236199275446914
                317179356373398118618
                542673939694417586329
                987342622289291613318
                971977649141188759131
            """.trimIndent().lines()
            one(sample) shouldBe 1573

            val input = lines(quest, 1)
            one(input) shouldBe 0
        }

        test("two") {
            val sample = """
                4547488458944
                9786999467759
                6969499575989
                7775645848998
                6659696497857
                5569777444746
                968586@767979
                6476956899989
                5659745697598
                6874989897744
                6479994574886
                6694118785585
                9568991647449
            """.trimIndent().lines()
            two(sample) shouldBe 1090

            val input = lines(quest, 2)
            two(input) shouldBe 65169
        }

        test("three") {
            val sample = """
            """.trimIndent().lines()
            three(sample) shouldBe 0

//            val input = lines(quest, 3)
//            three(input) shouldBe 0
        }
    }
}
