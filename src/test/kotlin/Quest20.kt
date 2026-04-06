import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest20 {
    private fun parse(input: List<String>) = CharArea(input)

    fun one(input: List<String>): Int {
        val area = parse(input)
        var jumps = 0
        area.tiles('T').forEach { p ->
            if (area.getOrNull(p.move(Direction.E)) == 'T') jumps++
            if (p.x % 2 != p.y % 2 && area.getOrNull(p.move(Direction.S)) == 'T') jumps++
        }
        return jumps
    }

    fun CharArea.turnRight(): CharArea {
        val next = clone('.')
        var nx = next.xRange.last
        var ny = 0
        var ax = 0
        var ay = 0
        var sx = 0
        fun valid() = nx in xRange && ny in yRange && ax in xRange && ay in yRange && this[ax, ay] != '.'
        do {
            next[nx--, ny] = this[ax++,ay]
            if (valid()) next[nx--, ny] = this[ax,ay++]
            if (!valid()) {
                ny++
                nx = next.xRange.last - ny
                sx += 2
                ax = sx
                ay = 0
            }
        } while (valid())
        return next
    }

    fun two(input: List<String>): Int {
        val area = parse(input)
        val connects = mutableSetOf<Pair<Point, Point>>()
        val start = area.first('S')
        val end = area.first('E')
        area[start] = 'T'
        area[end] = 'T'
        area.tiles('T').forEach { p ->
            val e = p.move(Direction.E)
            if (area.getOrNull(e) == 'T') {
                connects.add(p to e)
                connects.add(e to p)
            }
            if (p.x % 2 != p.y % 2) {
                val s = p.move(Direction.S)
                if (area.getOrNull(s) == 'T') {
                    connects.add(p to s)
                    connects.add(s to p)
                }
            }
        }
        return bfs(start) { p -> connects.filter { it.first == p }.map { it.second } }.first { it.value == end }.index
    }

    fun three(input: List<String>): Int {
        val first = parse(input)
        val start = first.first('S')
        first[start] = 'T'
        val areas = mutableListOf(first)
        areas += areas.last().turnRight()
        areas += areas.last().turnRight()
        val connects = mutableListOf<MutableSet<Pair<Point, Point>>>()
        val ends = mutableListOf<Point>()
        areas.forEachIndexed { i, area ->
            val end = area.first('E')
            ends += end
            area[end] = 'T'
            val connect = mutableSetOf<Pair<Point, Point>>()
            connects += connect
            area.tiles('T').forEach { p ->
                val e = p.move(Direction.E)
                if (area.getOrNull(e) == 'T') {
                    connect.add(p to e)
                    connect.add(e to p)
                }
                if (p.x % 2 != p.y % 2) {
                    val s = p.move(Direction.S)
                    if (area.getOrNull(s) == 'T') {
                        connect.add(p to s)
                        connect.add(s to p)
                    }
                }
            }
        }
        return sequence {
            val seen = mutableSetOf(start)
            val queue = ArrayDeque<IndexedValue<Point>>()
            queue.add(IndexedValue(0, start))
            while (queue.isNotEmpty()) {
                val a = queue.removeFirst()
                yield(a)
                for (b in connects[a.index % 3].filter { it.first == a.value }.map { it.second }) {
                    if (seen.add(b)) {
                        queue.add(IndexedValue(a.index + 1, b))
                    }
                }
            }
        }.first {
            if (it.index == 23) {
                println(it)
            }
            it.value == ends[it.index % 3]
        }.index
    }
}

val Quest20Test by testSuite {
    val quest = "20"

    with(Quest20) {
        test("one") {
            val sample1 = """
                T#TTT###T##
                .##TT#TT##.
                ..T###T#T..
                ...##TT#...
                ....T##....
                .....#.....
            """.trimIndent().lines()
            one(sample1) shouldBe 7

            val sample2 = """
                T#T#T#T#T#T
                .T#T#T#T#T.
                ..T#T#T#T..
                ...T#T#T...
                ....T#T....
                .....T.....
            """.trimIndent().lines()
            one(sample2) shouldBe 0

            val sample3 = """
                T#T#T#T#T#T
                .#T#T#T#T#.
                ..#T###T#..
                ...##T##...
                ....#T#....
                .....#.....
            """.trimIndent().lines()
            one(sample3) shouldBe 0

            val input = lines(quest, 1)
            one(input) shouldBe 120
        }

        test("two") {
            val sample = """
                TTTTTTTTTTTTTTTTT
                .TTTT#T#T#TTTTTT.
                ..TT#TTTETT#TTT..
                ...TT#T#TTT#TT...
                ....TTT#T#TTT....
                .....TTTTTT#.....
                ......TT#TT......
                .......#TT.......
                ........S........
            """.trimIndent().lines()
            two(sample) shouldBe 32

            val input = lines(quest, 2)
            two(input) shouldBe 569
        }

        test("three") {
//            val a = CharArea("""
//                12345
//                .678.
//                ..9..
//            """.trimIndent())
//            val b = a.turnRight()
            val sample = """
                T####T#TTT##T##T#T#
                .T#####TTTT##TTT##.
                ..TTTT#T###TTTT#T..
                ...T#TTT#ETTTT##...
                ....#TT##T#T##T....
                .....#TT####T#.....
                ......T#TT#T#......
                .......T#TTT.......
                ........TT#........
                .........S.........
            """.trimIndent().lines()
            three(sample) shouldBe 23

//            val input = lines(quest, 3)
//            three(input) shouldBe 0
        }
    }
}
