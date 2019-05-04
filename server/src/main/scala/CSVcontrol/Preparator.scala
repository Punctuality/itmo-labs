package CSVcontrol

object Preparator {
  def main(args: Array[String]): Unit = {
    val out = " <Compilers.SOS> Незнайка последовал совету Строптивого и растянулся на полке, не снимая одежды . <Compilers.EOS> \n <Compilers.SOS> он почувствовал, что на него напали зверушки и принялись кусать . <Compilers.EOS> \n <Compilers.SOS> Незнайка чесался, чуть до не крови тело, но это помогало . <Compilers.EOS> \n <Compilers.SOS> Незнайке, однако не хотелось дожидаться, когда клопы насытятся его кровью . <Compilers.EOS> \n <Compilers.SOS> правда, некоторое время он терпел, а потом соскочил на пол и принялся сбрасывать этих насекомых . <Compilers.EOS> \n <Compilers.SOS> воздух внизу был не такой душный, поэтому Незнайка решил сидеть всю ночь на полу . <Compilers.EOS>"

    val out2 = out.trim.replace("<Compilers.SOS>", "").replace("<Compilers.EOS>", "").split(" ").filter(_ != "")

    val wcsv = new CSVWriter("/home/sergey/Documents/TinkoffFintech/Labor5/resources/startText.csv")
    wcsv.writeSeq(out2.map(Array(_)))
    wcsv.close()

    val rcsv = new CSVReader("/home/sergey/Documents/TinkoffFintech/Labor5/resources/startText.csv")

    println(rcsv.readAll.foreach(elem => println(elem(0))))

    rcsv.close()
  }
}
