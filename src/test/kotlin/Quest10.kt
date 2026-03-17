import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest10 {
    private fun parse(input: List<String>) = CharArea(input)

    private fun move(start: Point, area: CharArea, prev: Set<Point> = emptySet()): Set<Point> =
        listOf(-2 to -1, -2 to 1, 2 to -1, 2 to 1, -1 to -2, -1 to 2, 1 to -2, 1 to 2)
            .map { (x, y) -> start.move(x, y) }
            .filter { it !in prev && it in area }
            .toSet()

    fun one(input: List<String>, rounds: Int = 4): Int {
        val area = parse(input)
        val dragon = area.first('D')
        var pos = setOf(dragon)
        val all = mutableSetOf(dragon)
        repeat(rounds) {
            pos = pos.flatMap { move(it, area, all) }.toSet()
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
            pos = pos.flatMap { move(it, area) }.toSet()
            val sheep = area.tiles('S').toMutableSet()
            val eaten = mutableSetOf<Point>()
            sheep.forEach { sheep ->
                area[sheep] = '.'
                if (sheep in pos && sheep !in hideouts) eaten += sheep
            }
            sheep -= eaten
            result += eaten.size
            sheep.forEach { current ->
                val next = current.move(Direction.S)
                if (next in pos && next !in hideouts) {
                    area[next] = '.'
                    result++
                } else if (next in area) {
                    area[next] = 'S'
                }
            }
        }
        return result
    }

    fun three(input: List<String>): Long {
        return 0L
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
            val sample1 = """
                SSS
                ..#
                #.#
                #D.
            """.trimIndent().lines()
            three(sample1) shouldBe 15L

            val sample2 = """
                SSS
                ..#
                ..#
                .##
                .D#
            """.trimIndent().lines()
            three(sample2) shouldBe 8L

            val sample3 = """
                ..S..
                .....
                ..#..
                .....
                ..D..
            """.trimIndent().lines()
            three(sample3) shouldBe 44L

            val sample4 = """
                .SS.S
                #...#
                ...#.
                ##..#
                .####
                ##D.#
            """.trimIndent().lines()
            three(sample4) shouldBe 4406L

            val sample5 = """
                SSS.S
                .....
                #.#.#
                .#.#.
                #.D.#
            """.trimIndent().lines()
            three(sample5) shouldBe 13033988838L

//            val input = lines(quest, 3)
//            three(input) shouldBe ""
        }
    }
}
