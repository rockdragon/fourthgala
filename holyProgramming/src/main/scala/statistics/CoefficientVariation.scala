
package statistics

object CoefficientVariation extends App {
  val athletesScores = Seq(
    AthleticScores("纳塔利娅·帕杰林娜", Seq(10.0, 8.5, 10.0, 10.2, 10.6, 10.5, 9.8, 9.7, 9.5, 9.3)),
    AthleticScores("郭文珺", Seq(10.0, 10.5, 10.4, 10.4, 10.1, 10.3, 9.4, 10.7, 10.8, 9.7)),
    AthleticScores("卓格巴德拉赫·蒙赫珠勒", Seq(9.3, 10.0, 8.7, 8.3, 9.2, 9.5, 8.5, 10.7, 9.2, 9.2)),
    AthleticScores("妮诺·萨卢克瓦泽", Seq(9.8, 10.3, 10.0, 9.5, 10.2, 10.7, 10.4, 10.6, 9.1, 10.8)),
    AthleticScores("维多利亚·柴卡", Seq(9.3, 9.4, 10.4, 10.1, 10.2, 10.5, 9.2, 10.5, 9.8, 8.6)),
    AthleticScores("莱万多夫斯卡·萨贡", Seq(8.1, 10.3, 9.2, 9.9, 9.8, 10.4, 9.9, 9.4, 10.7, 9.6)),
    AthleticScores("亚斯娜·舍卡里奇", Seq(10.2, 9.6, 9.9, 9.9, 9.3, 9.1, 9.7, 10.0, 9.3, 9.9)),
    AthleticScores("米拉·内万苏", Seq(8.7, 9.3, 9.2, 10.3, 9.8, 10.0, 9.7, 9.9, 9.9, 9.7))
  )

  for(athlete <- athletesScores) {
    val average = Lib.average(athlete.scores)
    val sigma = Lib.standardVariation(athlete.scores)
    val vs = Lib.coefficientVariation(athlete.scores)
    println(s"${athlete.name}, \t平均环数: $average, \t标准差: $sigma, \t离散系数: $vs") // scalastyle:ignore
  }

}
