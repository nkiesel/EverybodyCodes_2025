import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest12 {
    private fun parse(input: List<String>) = CharArea(input)

    fun one(input: List<String>): Int {
        val area = parse(input)
        val alreadyBurned = setOf(Point(0, 0))
        return burn(area, alreadyBurned)
    }

    fun two(input: List<String>): Int {
        val area = parse(input)
        val alreadyBurned = setOf(Point(0, 0), Point(area.xRange.last, area.yRange.last))
        return burn(area, alreadyBurned)
    }

    private fun burn(area: CharArea, alreadyBurned: Set<Point>): Int {
        val burned = alreadyBurned.toMutableSet()
        val queue = ArrayDeque(burned)
        while (queue.isNotEmpty()) {
            val p = queue.removeFirst()
            val c = area[p]
            val n = p.neighbors4().filter { it in area && it !in burned && area[it] <= c }
            burned += n
            queue.addAll(n)
        }
        return burned.size
    }

    fun three(input: List<String>): Int {
        val area = parse(input)
        val alreadyBurned = mutableSetOf<Point>()

        fun burn(start: Point): Set<Point> {
            val burned = alreadyBurned.toSet().toMutableSet()
            burned += start
            val queue = ArrayDeque(listOf(start))
            while (queue.isNotEmpty()) {
                val p = queue.removeFirst()
                val c = area[p]
                val n = p.neighbors4().filter { it in area && it !in burned && area[it] <= c }
                burned += n
                queue.addAll(n)
            }
            return burned - alreadyBurned
        }

        repeat(3) {
            alreadyBurned += area.tiles().filter { it !in alreadyBurned }.map { burn(it) }.maxBy { it.size }
        }
        return alreadyBurned.size
    }
}

val Quest12Test by testSuite {
    val quest = "12"

    with(Quest12) {
        test("one") {
            val sample = """
                989601
                857782
                746543
                766789
            """.trimIndent().lines()
            one(sample) shouldBe 16

            val input = lines(quest, 1)
            one(input) shouldBe 254
        }

        test("two") {
            val sample = """
                9589233445
                9679121695
                8469121876
                8352919876
                7342914327
                7234193437
                6789193538
                6781219648
                5691219769
                5443329859
            """.trimIndent().lines()
            two(sample) shouldBe 58

            val input = lines(quest, 2)
            two(input) shouldBe 5684
        }

        test("three") {
            val sample1 = """
                5411
                3362
                5235
                3112
            """.trimIndent().lines()
            three(sample1) shouldBe 14

            val sample2 = """
                41951111131882511179
                32112222211518122215
                31223333322115122219
                31234444432147511128
                91223333322176121892
                61112222211166431583
                14661111166111111746
                11111119142122222177
                41222118881233333219
                71222127839122222196
                56111126279711111517
            """.trimIndent().lines()
            three(sample2) shouldBe 136

            val input = lines(quest, 3)
            three(input) shouldBe 4318
        }
    }
}
