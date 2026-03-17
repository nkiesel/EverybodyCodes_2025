import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest10 {
    private fun parse(input: List<String>) = CharArea(input)

    private fun move(start: Point, area: CharArea, prev: Set<Point>): Set<Point> {
        return buildSet {
            for ((x, y) in listOf(-2 to -1, -2 to 1, 2 to -1, 2 to 1, -1 to -2, -1 to 2, 1 to -2, 1 to 2)) {
                val n = start.move(x, y)
                if (area.valid(n) && n !in prev) add(n)
            }
        }
    }

    private fun move2(start: Point, area: CharArea): Set<Point> {
        return buildSet {
            for ((x, y) in listOf(-2 to -1, -2 to 1, 2 to -1, 2 to 1, -1 to -2, -1 to 2, 1 to -2, 1 to 2)) {
                val n = start.move(x, y)
                if (area.valid(n)) add(n)
            }
        }
    }

    fun one(input: List<String>, rounds: Int = 4): Int {
        val area = parse(input)
        val dragon = area.first('D')
        var pos = setOf(dragon)
        val all = mutableSetOf(dragon)
        repeat(rounds) {
            pos = buildSet {
                for (p in pos) {
                    addAll(move(p, area, all))
                }
            }
            all += pos
        }
        val count = all.count { area[it] == 'S' }
        return count
    }

    fun two(input: List<String>, rounds: Int = 20): Int {
        val area = parse(input)
        val dragon = area.first('D')
        area[dragon] = '.'
        val hideouts = area.tiles('#').toSet()
        hideouts.forEach { area[it] = '.' }
        var pos = setOf(dragon)
        var result = 0
        repeat(rounds) {
            pos = buildSet {
                for (p in pos) {
                    addAll(move(p, area, emptySet()))
                }
            }
            val sheep = area.tiles('S').toMutableSet()
            val eaten = mutableSetOf<Point>()
            sheep.forEach { sheep ->
                area[sheep] = '.'
                if (sheep in pos && sheep !in hideouts) eaten += sheep
            }
            sheep -= eaten
            result += eaten.size
            sheep.forEach { sheep ->
                val next = sheep.move(Direction.S)
                if (next in pos && next !in hideouts) {
                    area[next] = '.'
                    result++
                } else {
                    area[next] = 'S'
                }
            }
        }
        return result
    }

    fun three(input: List<String>): String {
        return ""
    }
}

val Quest10Test by testSuite {
    val quest = "10"

    with(Quest10) {
        test("one") {
            val sample = """
                ...SSS.......
                .S......S.SS.
                ..S....S...S.
                ..........SS.
                ..SSSS...S...
                .....SS..S..S
                SS....D.S....
                S.S..S..S....
                ....S.......S
                .SSS..SS.....
                .........S...
                .......S....S
                SS.....S..S..
            """.trimIndent().lines()
            one(sample, 3) shouldBe 27

            val input = lines(quest, 1)
            one(input) shouldBe 155
        }

        test("two") {
            val sample = """
                ...SSS##.....
                .S#.##..S#SS.
                ..S.##.S#..S.
                .#..#S##..SS.
                ..SSSS.#.S.#.
                .##..SS.#S.#S
                SS##.#D.S.#..
                S.S..S..S###.
                .##.S#.#....S
                .SSS.#SS..##.
                ..#.##...S##.
                .#...#.S#...S
                SS...#.S.#S..
            """.trimIndent().lines()
            two(sample, 3) shouldBe 27

            val input = lines(quest, 2)
            two(input) shouldBe 1719
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
