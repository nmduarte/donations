import java.time.format.DateTimeFormatter
import java.time.LocalDate

/**
  * Case class Contribution
  *
  * @param name Name of the contributor
  * @param zipCode ZipCode of the contributor
  * @param amount Amount given
  * @param transactionDate Transaction Data
  * @param recipientID RecipientID
  */
case class Contribution(name: String, zipCode: String, amount: Long, transactionDate: LocalDate, recipientID: String) {
  /**
    * Identifies if it's the same donor based on name and zipcode
    * @param that Contribution
    * @return true if same donor, false otherwise
    */
  def sameDonor(that: Contribution): Boolean = name == that.name && zipCode == that.zipCode

  /**
    * Identifies contributions for the same recipient, from the same zipocode, in the same year
    * @param contribution Contribution
    * @return true if going to same recipient, on the same year, and from the same zipcode
    */
  def sameZipRecpYear(contribution: Contribution): Boolean = zipCode == contribution.zipCode &&
    recipientID == contribution.recipientID &&
    transactionDate.getYear == contribution.transactionDate.getYear
}

object Contribution{

  /**
    * based on header from https://classic.fec.gov/finance/disclosure/metadata/indiv_header_file.csv
    */
  val rawHeader =
    "CMTE_ID,AMNDT_IND,RPT_TP,TRANSACTION_PGI,IMAGE_NUM,TRANSACTION_TP,"+
      "ENTITY_TP,NAME,CITY,STATE,ZIP_CODE,EMPLOYER,OCCUPATION,TRANSACTION_DT,"+
      "TRANSACTION_AMT,OTHER_ID,TRAN_ID,FILE_NUM,MEMO_CD,MEMO_TEXT,SUB_ID"

  /*split the header*/
  val header = rawHeader.split(",")

  /**
    * Function to parse the lines on the input source
    * All the exclusion fields are tested in order to understand if the record should be considered or not.
    * If any of the tests fails, then None is return, otherwise, a Contribution is returned with the information on the file
    *
    * @param rawContribution line from source data file
    * @return None if there is any problem with the source data or Contribution with source data if everything is fine
    */
  def apply(rawContribution: String): Option[Contribution] = {

    val allFields = rawContribution.split('|') // split a pipe separated line to an array
    try {
      assert(allFields.length == header.length) //check that input line conforms the required format
      val otherID = allFields(header.indexOf("OTHER_ID"))
      assert(otherID.isEmpty)  //
      val name = allFields(header.indexOf("NAME"))
      assert(name.nonEmpty)
      val rawZipCode = allFields(header.indexOf("ZIP_CODE"))
      assert(rawZipCode.nonEmpty)
      assert(rawZipCode.length >= 5)
      val zipCode = rawZipCode.substring(0, 5)
      val amount = allFields(header.indexOf("TRANSACTION_AMT"))
      assert(amount.nonEmpty)
      val formatter = DateTimeFormatter.ofPattern("MMddyyyy")
      val rawDate = allFields(header.indexOf("TRANSACTION_DT"))
      assert(rawDate.nonEmpty)
      val transactionDate = LocalDate.parse(rawDate, formatter)
      val recipientID = allFields(header.indexOf("CMTE_ID"))
      assert(recipientID.nonEmpty)
      Some(Contribution(name, zipCode, amount.toLong, transactionDate, recipientID))
    }catch {
      case _ => None //if there is an assertion error or parsing exception, the function returns None (to be ignored using flatmap)
    }
  }
}