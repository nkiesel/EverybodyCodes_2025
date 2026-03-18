import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.aroundAll
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import jdk.internal.vm.vector.VectorSupport.test
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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
        var sheep = area.tiles('S').toSet()
        var pos = setOf(dragon)
        var result = 0
        repeat(rounds) {
            pos = pos.flatMap { move(it, area) }.toSet()
            val eaten = sheep.filter { it in pos && it !in hideouts }.toSet()
            result += eaten.size
            sheep = buildSet {
                (sheep - eaten).map { it.move(Direction.S) }.forEach {
                    if (it in pos && it !in hideouts) result++ else if (it in area) add(it)
                }
            }
        }
        return result
    }

    private fun Point.s(p: Char) = " $p>${('A'.code + x).toChar()}${y + 1}"

    fun three(input: List<String>): Long {
        val area = parse(input)
        val hideouts = area.tiles('#').toSet()

        class State3(val dragon: Point, var sheep: List<Point>, val steps: String, var eaten: List<Point>) {
            var movedOut = false
            fun moves(): List<State3> {
                val nextDragon = move(dragon, area)
                var states = buildList {
                    for (s in sheep) {
                        val n = s.move(Direction.S)
                        if (n !in area) {
                            movedOut = true
                        } else if (n != dragon || n in hideouts) {
                            val ns = sheep - s + n
                            if (ns.any { it.y != area.yRange.last }) {
                                nextDragon.forEach { d -> add(State3(d, ns, steps + n.s('S') + d.s('D'), eaten)) }
                            }
                        }
                    }
                }
                if (states.isEmpty() && !movedOut) {
                    states = nextDragon.map { State3(it, sheep, steps + it.s('D'), eaten) }
                }
                return states
            }
        }

        val state = State3(area.first('D'), area.tiles('S').toList(), "", emptyList())
        val queue = ArrayDeque(listOf(state))
        var processed = 0
        var result = 0L
        val debug = mutableListOf<String>()
        while (queue.isNotEmpty()) {
            val s = queue.removeLast()
            processed++
            val eaten = s.sheep.find { it == s.dragon && it !in hideouts }
            if (eaten != null) {
                s.eaten += eaten
                s.sheep -= eaten
            }
            if (s.sheep.isEmpty()) {
                debug += s.steps
                println(s.steps)
                result++
            } else {
                s.moves().forEach { queue.add(it) }
            }
        }
        return result
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

        val configuration = TestConfig.aroundAll { elementAction ->
            withTimeout(30.minutes) {
                elementAction()
            }
        }

        test("three", testConfig = configuration) {
            val sample1 = """
                SSS
                ..#
                #.#
                #D.
            """.trimIndent().lines()
            println("sample1")
            three(sample1) shouldBe 15L

            val sample2 = """
                SSS
                ..#
                ..#
                .##
                .D#
            """.trimIndent().lines()
            println("sample2")
            three(sample2) shouldBe 8L
//
//            val sample3 = """
//                ..S..
//                .....
//                ..#..
//                .....
//                ..D..
//            """.trimIndent().lines()
//            three(sample3) shouldBe 44L

//            val sample4 = """
//                .SS.S
//                #...#
//                ...#.
//                ##..#
//                .####
//                ##D.#
//            """.trimIndent().lines()
//            three(sample4) shouldBe 4406L

//            val sample5 = """
//                SSS.S
//                .....
//                #.#.#
//                .#.#.
//                #.D.#
//            """.trimIndent().lines()
//            three(sample5) shouldBe 13033988838L

//            val input = lines(quest, 3)
//            three(input) shouldBe ""
        }
    }
}
