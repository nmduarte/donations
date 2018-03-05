import org.scalatest.FunSuite

import scala.io.Source



class TestSuit1 extends FunSuite {

  def testAnalyser(description: String, inputFile: String, percentilFile: String, outputFile: String, expectedOutputFile:String) = {
    test(description) {
      donationAnalyser.analyse(inputFile,percentilFile,outputFile)
      assert (Source.fromFile(outputFile).mkString == Source.fromFile(expectedOutputFile).mkString)
    }
  }

  testAnalyser("default Sample", "data/test_1/input/itcont.txt", "data/test_1/input/percentile.txt","data/test_1/output.txt", "data/test_1/output/repeat_donors.txt")
}