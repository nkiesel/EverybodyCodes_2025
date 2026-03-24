import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest16 {
    private fun parse(input: List<String>) = input[0].ints()

    fun one(input: List<String>): Int {
        val nums = parse(input)
        val blocks = IntArray(90)
        for (n in nums) {
            var i = -1
            while (true) {
                i += n
                if (i >= blocks.size) break
                blocks[i] = blocks[i] + 1
            }
        }
        println(blocks.joinToString(","))
        return blocks.sum()
    }

    private fun one1(nums: List<Int>, size: Int): String {
        val blocks = IntArray(size)
        for (n in nums) {
            var i = -1
            while (true) {
                i += n
                if (i >= blocks.size) break
                blocks[i] = blocks[i] + 1
            }
        }
        return blocks.joinToString(",")
    }

    fun two(input: List<String>): Long {
        val blocks = parse(input).toIntArray()
        val spell = mutableListOf<Long>()
        while (true) {
            val i = blocks.indexOfFirst { it != 0 } + 1
            if (i == 0) break
            spell += i.toLong()
            var m = i - 1
            while (m < blocks.size) {
                blocks[m]--
                m += i
            }
        }
        return spell.product()
    }

    fun three(input: List<String>): Long {
        val blocks = parse(input).toIntArray()
        val spell = mutableListOf<Long>()
        while (true) {
            val i = blocks.indexOfFirst { it != 0 } + 1
            if (i == 0) break
            spell += i.toLong()
            var m = i - 1
            while (m < blocks.size) {
                blocks[m]--
                m += i
            }
        }
        var w = 1L
        var c = 0L
        var p = 0L
        val l = spell.last()
        var d = 0L
        var l1 = 0L
        var l2 = 0L
        var l3 = 0L
        var l5 = 0L
        val limit = 202520252025000L
        var skip = false
        while (true) {
            for (s in spell) {
                if (w % s == 0L) c++
            }
            if (!skip && w % l == 0L) {
                if (l1 == 0L) {
                    l1 = c
                } else if (c - p == l1) {
                    if (w < 300L) println("${w / l}: c=$c w=$w ${c - d} ${c - p}")
                    if (l2 == 0L) {
                        l2 = c - l1
                    } else if (l3 == 0L) {
                        l3 = w / l
                        l5 = w
                        println("l3=$l3")
                    } else {
                        val l4 = w / l - l3
                        if (w < 300L) println("l1=$l1 l2=$l2 l3=$l3 l4=$l4")
                        val rep = (limit - c) / l2
                        if (w < 300L) println("rep=$rep c=$c w=$w")
                        c += rep * l2
                        w += rep * (w - l5)
                        println("c=$c w=$w")
                        skip = true
                    }
                    d = c
                }
                p = c
            }
            if (c > limit) break
//            if (c > 202520252025000L) break
            w++
        }
        println("l1=$l1 l2=$l2 w=$w c=$c")
        return w - 1L
    }
}

val Quest16Test by testSuite {
    val quest = "16"

    with(Quest16) {
        test("one") {
            val sample = """
                1,2,3,5,9
            """.trimIndent().lines()
            one(sample) shouldBe 193

            val input = lines(quest, 1)
            one(input) shouldBe 231
        }

        test("two") {
            val sample = """
                1,2,2,2,2,3,1,2,3,3,1,3,1,2,3,2,1,4,1,3,2,2,1,3,2,2,3,2,1,4,1,2,2,2,2,4,1,2,2,3,1,3,1,2,4,2,1,3,1,3,2,2,1,4,2,2,2,2,1,4,1,2,3,2,2,3,1,2,2,3,1,4,1,2,3,2,1,3,1,3,3,2,1,3,2,2,2,2,1,5
            """.trimIndent().lines()
            two(sample) shouldBe 270L

            val input = lines(quest, 2)
            two(input) shouldBe 107993174016L
        }

        test("three") {
            val sample = """
                1,2,2,2,2,3,1,2,3,3,1,3,1,2,3,2,1,4,1,3,2,2,1,3,2,2
            """.trimIndent().lines()
            three(sample) shouldBe 94439495762954L

            val input = lines(quest, 3)
            three(input) shouldBe 0L
        }
    }
}
