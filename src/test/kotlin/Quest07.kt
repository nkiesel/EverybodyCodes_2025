import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest07 {
    private fun parse(input: List<String>): Pair<List<String>, Map<Char, Set<Char>>> =
        Pair(input[0].split(","), buildMap {
            for (line in input.drop(2)) {
                put(line[0], line.substringAfter("> ").replace(",", "").toSet())
            }
        })

    private fun validName(name: String, rules: Map<Char, Set<Char>>) =
        name.zipWithNext().all { (a, b) -> rules[a]?.contains(b) == true }

    fun one(input: List<String>): String {
        val (list, rules) = parse(input)
        return list.first { validName(it, rules) }
    }

    fun two(input: List<String>): Int {
        val (list, rules) = parse(input)
        return list.mapIndexed { i, n ->
            if (validName(n, rules)) i + 1 else 0
        }.sum()
    }

    fun three(input: List<String>): Int {
        val (list, rules) = parse(input)
        val names = mutableSetOf<String>()
        fun extend(name: String) {
            if (name.length <= 11 && (name.length < 7 || names.add(name))) {
                rules[name.last()]?.map { name + it }?.forEach { extend(it) }
            }
        }
        list.filter { validName(it, rules) }.forEach { extend(it) }
        return names.size
    }
}

val Quest07Test by testSuite {
    val quest = "07"

    with(Quest07) {
        test("one") {
            val sample = """
                Oronris,Urakris,Oroneth,Uraketh

                r > a,i,o
                i > p,w
                n > e,r
                o > n,m
                k > f,r
                a > k
                U > r
                e > t
                O > r
                t > h
            """.trimIndent().lines()
            one(sample) shouldBe "Oroneth"

            val input = lines(quest, 1)
            one(input) shouldBe "Kymirath"
        }

        test("two") {
            val sample = """
                Xanverax,Khargyth,Nexzeth,Helther,Braerex,Tirgryph,Kharverax

                r > v,e,a,g,y
                a > e,v,x,r
                e > r,x,v,t
                h > a,e,v
                g > r,y
                y > p,t
                i > v,r
                K > h
                v > e
                B > r
                t > h
                N > e
                p > h
                H > e
                l > t
                z > e
                X > a
                n > v
                x > z
                T > i
            """.trimIndent().lines()
            two(sample) shouldBe 23

            val input = lines(quest, 2)
            two(input) shouldBe 1784
        }

        test("three") {
            val sample1 = """
                Xaryt

                X > a,o
                a > r,t
                r > y,e,a
                h > a,e,v
                t > h
                v > e
                y > p,t
            """.trimIndent().lines()
            three(sample1) shouldBe 25

            val sample2 = """
                Khara,Xaryt,Noxer,Kharax

                r > v,e,a,g,y
                a > e,v,x,r,g
                e > r,x,v,t
                h > a,e,v
                g > r,y
                y > p,t
                i > v,r
                K > h
                v > e
                B > r
                t > h
                N > e
                p > h
                H > e
                l > t
                z > e
                X > a
                n > v
                x > z
                T > i
            """.trimIndent().lines()
            three(sample2) shouldBe 1154

            val input = lines(quest, 3)
            three(input) shouldBe 8581494
        }
    }
}
