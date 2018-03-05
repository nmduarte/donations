import java.io.PrintWriter

import scala.io.Source

object donationAnalyser {
  def main(args: Array[String]): Unit = {
    val inputFile = args(0)//"insight_testsuite/tests/test_1/input/itcont.txt"
    val percentileFile = args(1)//"insight_testsuite/tests/test_1/input/percentile.txt"
    val outputFile = args(2)//"insight_testsuite/tests/test_1/output/repeat_donors.txt"
    analyse( inputFile, percentileFile, outputFile)
  }

  /**
    * Function to analyse and return the contributions
    *
    * @param inputFile File containing the source data
    * @param percentileFile File containing the percentile
    * @param outputFile Output file where the results are to be written
    */
  def analyse(inputFile: String, percentileFile: String,  outputFile: String) = {
    val writer = new PrintWriter(outputFile)
    val contributions = Source.fromFile(inputFile).getLines().flatMap(Contribution(_))  //read all the contributions, parse them and flatten them to ignore the faulty ones
    val sortedContributions = contributions.toList.sortWith((a, b) => a.transactionDate.isBefore(b.transactionDate)) // sort them by date
    val p = Source.fromFile(percentileFile).getLines().next().trim.toInt //read percentile
    for (i <- sortedContributions.indices) { //traverse contributions in a chronological order
      val contribution = sortedContributions(i) // for each contribution
      val previousContributions = sortedContributions.splitAt(i)._1 // get contributions previous to that one
      val previousContributionsFromDonor = previousContributions.filter(_.sameDonor(contribution)) //from those previous contributions get those that have the same donor as "contribution"
      if (previousContributionsFromDonor.nonEmpty) { // it is a repeatDonor
        val relevantContributions = previousContributions.filter(_.sameZipRecpYear(contribution)) :+ contribution // get previous contributions that have same (ZipCode,CMTE_ID,year) as "contribution" and add "contribution" to that list
        val numberContributions = relevantContributions.size // number of contributions
        val total = relevantContributions.foldLeft(0L)(_ + _.amount) // sum the amounts of relevantContributions
        val percentileIdx = Math.ceil(1.0 * numberContributions * p / 100).toInt - 1 //compute the percentile index
        val percentile = relevantContributions(percentileIdx).amount
        writer.println(s"${contribution.recipientID}|${contribution.zipCode}|${contribution.transactionDate.getYear}|$percentile|$total|$numberContributions") //print to output text
      }
    }
    writer.close()
  }
}
