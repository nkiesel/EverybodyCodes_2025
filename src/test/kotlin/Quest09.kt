import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest09 {
    private fun parse(input: List<String>) = input.map { it.substringAfter(':') }

    private fun isChild(c: String, p: List<String>): Boolean {
        return c.withIndex().all { (i, c) -> c == p[0][i] || c == p[1][i] }
    }

    private fun similarity(c: String, parents: List<String>): Int {
        return parents.map { c.zip(it).count { (a, b) -> a == b } }.product()
    }

    fun one(input: List<String>): Int {
        val dnas = parse(input)
        val (i, c) = dnas.withIndex().first { (i, l) ->
            isChild(l, dnas.filterIndexed { index, _ -> index != i })
        }
        return similarity(c, dnas.filterIndexed { index, _ -> index != i })
    }

    fun two(input: List<String>): Int {
        val dnas = parse(input)
        var result = 0
        for (i in dnas.indices) {
            val c = dnas[i]
            for (j in dnas.indices) {
                if (j == i) continue
                for (k in j + 1 until dnas.size) {
                    if (k == i) continue
                    val parents = listOf(dnas[j], dnas[k])
                    if (isChild(c, parents)) {
                        result += similarity(c, parents)
                    }
                }
            }
        }
        return result
    }

    fun three(input: List<String>): Int {
        val dnas = parse(input)
        var families = buildList {
            for (i in dnas.indices) {
                val c = dnas[i]
                for (j in dnas.indices) {
                    if (j == i) continue
                    for (k in j + 1 until dnas.size) {
                        if (k == i) continue
                        val parents = listOf(dnas[j], dnas[k])
                        if (isChild(c, parents)) {
                            add(setOf(i + 1, j + 1, k + 1))
                        }
                    }
                }
            }
        }
        while (true) {
            val alreadyJoined = mutableSetOf<Int>()
            val next = buildList {
                for (i in families.indices) {
                    if (i in alreadyJoined) continue
                    val f1 = families[i]
                    var joined = false
                    for (j in i + 1 until families.size) {
                        val f2 = families[j]
                        if (f1.any { it in f2 }) {
                            add(f1 + f2)
                            alreadyJoined += j
                            joined = true
                            break
                        }
                    }
                    if (!joined) add(f1)
                }
            }
            if (alreadyJoined.isEmpty()) break
            families = next
        }
        return families.maxBy { it.size }.sum()
    }
}

val Quest09Test by testSuite {
    val quest = "09"

    with(Quest09) {
        test("one") {
            val sample = """
                1:CAAGCGCTAAGTTCGCTGGATGTGTGCCCGCG
                2:CTTGAATTGGGCCGTTTACCTGGTTTAACCAT
                3:CTAGCGCTGAGCTGGCTGCCTGGTTGACCGCG
            """.trimIndent().lines()
            one(sample) shouldBe 414

            val input = lines(quest, 1)
            one(input) shouldBe 5810
        }

        test("two") {
            val sample = """
                1:GCAGGCGAGTATGATACCCGGCTAGCCACCCC
                2:TCTCGCGAGGATATTACTGGGCCAGACCCCCC
                3:GGTGGAACATTCGAAAGTTGCATAGGGTGGTG
                4:GCTCGCGAGTATATTACCGAACCAGCCCCTCA
                5:GCAGCTTAGTATGACCGCCAAATCGCGACTCA
                6:AGTGGAACCTTGGATAGTCTCATATAGCGGCA
                7:GGCGTAATAATCGGATGCTGCAGAGGCTGCTG
            """.trimIndent().lines()
            two(sample) shouldBe 1245

            val input = lines(quest, 2)
            two(input) shouldBe 318562
        }

        test("three") {
            val sample1 = """
                1:GCAGGCGAGTATGATACCCGGCTAGCCACCCC
                2:TCTCGCGAGGATATTACTGGGCCAGACCCCCC
                3:GGTGGAACATTCGAAAGTTGCATAGGGTGGTG
                4:GCTCGCGAGTATATTACCGAACCAGCCCCTCA
                5:GCAGCTTAGTATGACCGCCAAATCGCGACTCA
                6:AGTGGAACCTTGGATAGTCTCATATAGCGGCA
                7:GGCGTAATAATCGGATGCTGCAGAGGCTGCTG
            """.trimIndent().lines()
            three(sample1) shouldBe 12

            val sample2 = """
                1:GCAGGCGAGTATGATACCCGGCTAGCCACCCC
                2:TCTCGCGAGGATATTACTGGGCCAGACCCCCC
                3:GGTGGAACATTCGAAAGTTGCATAGGGTGGTG
                4:GCTCGCGAGTATATTACCGAACCAGCCCCTCA
                5:GCAGCTTAGTATGACCGCCAAATCGCGACTCA
                6:AGTGGAACCTTGGATAGTCTCATATAGCGGCA
                7:GGCGTAATAATCGGATGCTGCAGAGGCTGCTG
                8:GGCGTAAAGTATGGATGCTGGCTAGGCACCCG
            """.trimIndent().lines()
            three(sample2) shouldBe 36

            val input = lines(quest, 3)
            three(input) shouldBe 35744
        }
    }
}
