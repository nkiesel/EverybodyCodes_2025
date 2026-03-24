import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest18 {
    private data class Branch(val from: Int, val to: Int, val thickness: Int)
    private data class Plant(val id: Int, val thickness: Int, var energy: Int? = null)

    private fun parse(input: List<String>): Pair<Map<Int, Plant>, List<Branch>> {
        val branches = mutableListOf<Branch>()
        val plants = mapOf(0 to Plant(0, 0).apply { energy = 1 }) + input.chunkedBy { it.isEmpty() }.associate { plant: List<String> ->
            val (p, t) = plant[0].ints()
            plant.drop(1).map { it.ints() }.forEach { b ->
                branches += Branch(if (b.size == 1) 0 else b.first(), p, b.last())
            }
            p to Plant(p, t)
        }
        return plants to branches
    }

    fun one(input: List<String>): Int {
        val (plants, branches) = parse(input)
        while (true) {
            val p = plants.values.find { p -> p.energy == null && branches.filter { it.to == p.id }.all { plants[it.from]!!.energy != null }}
            if (p == null) return plants.values.maxOf { it.energy ?: 0 }
            val energy = branches.filter { it.to == p.id }.sumOf { plants[it.from]!!.energy!! * it.thickness }
            p.energy = if (energy < p.thickness) 0 else energy
        }
    }

    fun two(input: List<String>): Int {
        return 0
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest18Test by testSuite {
    val quest = "18"

    with(Quest18) {
        test("one") {
            val sample = """
                Plant 1 with thickness 1:
                - free branch with thickness 1

                Plant 2 with thickness 1:
                - free branch with thickness 1

                Plant 3 with thickness 1:
                - free branch with thickness 1

                Plant 4 with thickness 17:
                - branch to Plant 1 with thickness 15
                - branch to Plant 2 with thickness 3

                Plant 5 with thickness 24:
                - branch to Plant 2 with thickness 11
                - branch to Plant 3 with thickness 13

                Plant 6 with thickness 15:
                - branch to Plant 3 with thickness 14

                Plant 7 with thickness 10:
                - branch to Plant 4 with thickness 15
                - branch to Plant 5 with thickness 21
                - branch to Plant 6 with thickness 34
            """.trimIndent().lines()
            one(sample) shouldBe 774

            val input = lines(quest, 1)
            one(input) shouldBe 1043844
        }

        test("two") {
            val sample = """
            """.trimIndent().lines()
            two(sample) shouldBe 0

//            val input = lines(quest, 2)
//            two(input) shouldBe 0
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
