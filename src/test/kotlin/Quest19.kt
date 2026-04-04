import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest19 {
    private fun parse(input: List<String>) = input.map { it.ints() }

    fun one(input: List<String>): Int {
        val data = parse(input)
        val maxX = data.maxOf { it[0] + 2 }
        val maxY = data.maxOf { it[1] + it[2] + 2 }
        val area = CharArea(maxX, maxY, '.')
        val start = Point(0, maxY)
        area[start] = 'S'
        val openings = mutableListOf<List<Point>>()
        data.forEach { (x, y, r) ->
            val open = (maxY - y - r)..<(maxY - y)
            val op = buildList {
                for (y in 0..<maxY) {
                    if (y in open) {
                        add(Point(x, y))
                    } else {
                        area[x, y] = '#'
                    }
                }
            }
            openings += op
        }
        val up = '↑'
        val down = '↓'
        var ups = 0
        var pos = start
        openings.forEachIndexed { index, points ->
            while (pos.x < data[index][0]) {
                if (pos.y > points.last().y) {
                    pos = pos.move(Direction.NE)
                    area[pos] = up
                    ups++
                } else {
                    pos = pos.move(Direction.SE)
                    area[pos] = down
                }
            }
        }

        return ups
    }

    fun two(input: List<String>): Int {
        val data = parse(input)
        val maxY = data.maxOf { it[1] + it[2] + 2 }
        val openings = mutableMapOf<Int, MutableSet<Int>>()
        data.forEach { (x, y, r) ->
            openings.getOrPut(x) { mutableSetOf() }.addAll((maxY - y - r)..<(maxY - y))
        }
        var ups = 0
        var x = 0
        var y = maxY
        openings.entries.sortedBy { it.key }.forEach { (index, points) ->
            do {
                if (y > points.max()) {
                    y--
                    ups++
                } else {
                    y++
                }
            } while (++x < index)
        }
        return ups
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest19Test by testSuite {
    val quest = "19"

    with(Quest19) {
        test("one") {
            val sample = """
                7,7,2
                12,0,4
                15,5,3
                24,1,6
                28,5,5
                40,8,2
            """.trimIndent().lines()
            one(sample) shouldBe 24

            val input = lines(quest, 1)
            one(input) shouldBe 57
            two(input) shouldBe 57
        }

        test("two") {
            val sample = """
                7,7,2
                7,1,3
                12,0,4
                15,5,3
                24,1,6
                28,5,5
                40,3,3
                40,8,2
            """.trimIndent().lines()
            two(sample) shouldBe 22

            val input = lines(quest, 2)
            two(input) shouldBe 753
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
