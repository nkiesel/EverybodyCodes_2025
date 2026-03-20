import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest15 {
    private fun parse(input: List<String>) = input[0].split(",")

    fun one(input: List<String>): Int {
        val steps = parse(input)
        var p = Point(0, 0)
        var dir = Direction.N
        val points = mutableListOf(p)
        for (step in steps) {
            dir = if (step[0] == 'R') dir.turnRight() else dir.turnLeft()
            repeat(step.substring(1).toInt()) {
                p = p.move(dir)
                points += p
            }
        }
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val area = CharArea(maxX - minX + 3, maxY - minY + 3, ' ')
        for ((i, p) in points.withIndex()) {
            area[p.x - minX + 2, p.y - minY + 2] = when (i) {
                0 -> 'S'
                points.lastIndex -> 'E'
                else -> '#'
            }
        }
        val start = area.first('S')
        val end = area.first('E')
        return bfs(start) { p -> area.neighbors4(p) { it != '#' } }.first { it.value == end }.index
    }

    fun two(input: List<String>): Int {
        val steps = parse(input)
        var p = Point(0, 0)
        var dir = Direction.N
        val points = mutableListOf(p)
        for (step in steps) {
            dir = if (step[0] == 'R') dir.turnRight() else dir.turnLeft()
            repeat(step.substring(1).toInt()) {
                p = p.move(dir)
                points += p
            }
        }
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val area = CharArea(maxX - minX + 3, maxY - minY + 3, ' ')
        for ((i, p) in points.withIndex()) {
            area[p.x - minX + 2, p.y - minY + 2] = when (i) {
                0 -> 'S'
                points.lastIndex -> 'E'
                else -> '#'
            }
        }
        val start = area.first('S')
        val end = area.first('E')
        return bfs(start) { p -> area.neighbors4(p) { it != '#' } }.first { it.value == end }.index
    }

    fun three(input: List<String>): Int {
        val steps = parse(input)
        val min = steps.minOf { it.substring(1).toInt() }
//        val min = gcd(steps.map { it.substring(1).toInt() }.toSet().toList())
        var p = Point(0, 0)
        var dir = Direction.N
        val points = mutableListOf(p)
        for (step in steps) {
            dir = if (step[0] == 'R') dir.turnRight() else dir.turnLeft()
            repeat(step.substring(1).toInt() / min * 4) {
                p = p.move(dir)
                points += p
            }
        }
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val area = CharArea(maxX - minX + 3, maxY - minY + 3, ' ')
        for ((i, p) in points.withIndex()) {
            area[p.x - minX + 2, p.y - minY + 2] = when (i) {
                0 -> 'S'
                points.lastIndex -> 'E'
                else -> '#'
            }
        }
        println("min = $min")
        area.png()
        println("------------------------")
//        val start = area.first('S')
//        val end = area.first('E')
//        return bfs(start) { p -> area.neighbors4(p) { it != '#' } }.first { it.value == end }.index
        return 0
    }
}

val Quest15Test by testSuite {
    val quest = "15"

    with(Quest15) {
        test("one") {
            val sample1 = """
                R3,R4,L3,L4,R3,R6,R9
            """.trimIndent().lines()
            one(sample1) shouldBe 6

            val sample2 = """
                L6,L3,L6,R3,L6,L3,L3,R6,L6,R6,L6,L6,R3,L3,L3,R3,R3,L6,L6,L3
            """.trimIndent().lines()
            one(sample2) shouldBe 16

            val input = lines(quest, 1)
            one(input) shouldBe 122
        }

        test("two") {
            val input = lines(quest, 2)
            two(input) shouldBe 4387
        }

        test("three") {
            val input = lines(quest, 3)
            // Issue is that the map is way too large. The comment says that the walls never intersect.  Therefore, it
            // should be possible to scale down the map, then find the path through the map, and then either figure out
            // how the scale-deon factor should be used to adjust the path length, or perhaps then use the path to walk
            // the same way through the original map.  However, dividing by min and then * 3 still ends up with walls
            // intersecting/overlapping, so that is not the correct scale factor.
            three(input) shouldBe 0
        }
    }
}
