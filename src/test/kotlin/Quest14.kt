import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest14 {
    private fun parse(input: List<String>) = CharArea(input)

    private fun oneTwo(input: List<String>, rounds: Int): Int {
        var area = parse(input)
        var result = 0
        repeat(rounds) {
            val next = area.clone()
            area.tiles().forEach { p ->
                val c = area[p]
                val even = (area.neighbors8(p, '#').toSet() - area.neighbors4(p, '#').toSet()).size % 2 == 0
                next[p] = when (c) {
                    '#' -> if (even) '.' else '#'
                    else -> if (even) '#' else '.'
                }
            }
            area = next
            result += area.count('#')
        }
        return result
    }

    fun one(input: List<String>) = oneTwo(input, 10)

    fun two(input: List<String>) = oneTwo(input, 2025)

    fun three(input: List<String>): Int {
        val center = parse(input)
        var remaining = 1000000000
        var round = 0
        val size = 34
        var area = CharArea(size, size, '.')
        val ox = (size - center.xRange.last) / 2
        val oy = (size - center.yRange.last) / 2
        var activeTiles = 0

        data class State(val round: Int, val activeTiles: Int, val fp: String)

        val seen = mutableListOf<State>()
        var notJumped = true
        do {
            val next = area.clone()
            val fpa = LongArray(size)
            var hasCenter = true
            area.tiles().forEach { p ->
                val c = area[p]
                val even = (area.neighbors8(p, '#').toSet() - area.neighbors4(p, '#').toSet()).size % 2 == 0
                val n = when (c) {
                    '#' -> if (even) '.' else '#'
                    else -> if (even) '#' else '.'
                }
                next[p] = n
                if (notJumped && n == '#') fpa[p.y] = fpa[p.y] or (1L shl p.x)
                val cp = Point(p.x - ox, p.y - oy)
                if (cp in center && center[cp] != n) hasCenter = false
            }
            area = next
            round++
            remaining--
            if (hasCenter) {
                activeTiles += area.count('#')
            }
            if (notJumped) {
                val fp = fpa.joinToString("") { it.toString() }
                val previous = seen.find { it.fp == fp }
                if (previous != null) {
                    val dr = round - previous.round
                    val rep = remaining / dr
                    remaining -= rep * dr
                    round += rep * dr
                    activeTiles += rep * (activeTiles - previous.activeTiles)
                    notJumped = false
                }
                seen += State(round, activeTiles, fp)
            }
        } while (remaining > 0)
        return activeTiles
    }
}

val Quest14Test by testSuite {
    val quest = "14"

    with(Quest14) {
        test("one") {
            val sample = """
                .#.##.
                ##..#.
                ..##.#
                .#.##.
                .###..
                ###.##
            """.trimIndent().lines()
            one(sample) shouldBe 200

            val input = lines(quest, 1)
            one(input) shouldBe 475
        }

        test("two") {
            val input = lines(quest, 2)
            two(input) shouldBe 1169606
        }

        test("three") {
            val sample = """
                #......#
                ..#..#..
                .##..##.
                ...##...
                ...##...
                .##..##.
                ..#..#..
                #......#
            """.trimIndent().lines()
            three(sample) shouldBe 278388552

            val input = lines(quest, 3)
            three(input) shouldBe 1029547864
        }
    }
}
