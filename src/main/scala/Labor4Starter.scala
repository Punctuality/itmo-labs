import Compilers.TextCompiler

object Labor4Starter {

  def main(args: Array[String]): Unit = {
    val Neznaika: Person = new Person("Незнайка Незнайки Незнайке Незнайку Незнайкой Незнайке")
    val Stroptivii: Person = new Person("Строптивый Строптивого Строптивому Стротивого Строптивым Строптивом")

    val Zveri: Person = new Person("зверушки зверушек зверушкам зверушек зверушками зверушках")
    val Klopi: Person = new Person("клопы клопов клопам клопов клопами клопах")
    val Nasokomii: Person = new Person("насекомые насекомых насекомым насекомых насекомыми насекомых")

    val textcomp: TextCompiler = new TextCompiler
    //First sentence
    textcomp.add(Neznaika.getName(0))
    textcomp.add(Neznaika.doAction("FOLLOWED"))
    textcomp.add(Conceptions.valueOf("ADVISE").getName(2))
    textcomp.add(Stroptivii.getName(1))
    textcomp.add(Prepositions.valueOf("AND").getLine)
    textcomp.add(Neznaika.doAction("STRECHED"))
    textcomp.add(Prepositions.valueOf("ON").getLine)
    textcomp.add(Places.valueOf("SHELF").getName(5))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("NOT").getLine)
    textcomp.add(Neznaika.doAction("TAKEOFF"))
    textcomp.add(Neznaika.getBodyPart("CLOTH", 1))
    textcomp.next()
    //Second sentence
    textcomp.add(Pronouns.valueOf("HE").getName(0))
    textcomp.add(Neznaika.doAction("FELT"))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("WHAT").getLine)
    textcomp.add(Prepositions.valueOf("ON").getLine)
    textcomp.add(Pronouns.valueOf("HE").getName(3))
    textcomp.add(Zveri.doAction("ATTACKED"))
    textcomp.add(Zveri.getName(0))
    textcomp.add(Prepositions.valueOf("AND").getLine)
    textcomp.add(Zveri.doAction("STARTEDP"))
    textcomp.add(Zveri.doAction("BITE"))
    textcomp.next()
    //Third Sentence
    textcomp.add(Neznaika.getName(0))
    textcomp.add(Neznaika.doAction("ITCHED"))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("ALMOST").getLine)
    textcomp.add(Prepositions.valueOf("TO").getLine)
    textcomp.add(Prepositions.valueOf("NOT").getLine)
    textcomp.add(Neznaika.getBodyPart("BLOOD", 5))
    textcomp.add(Neznaika.getBodyPart("BODY", 0))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("BUT").getLine)
    textcomp.add(Prepositions.valueOf("THAT").getLine)
    textcomp.add(Pronouns.valueOf("IT").doAction("HELPED"))
    textcomp.next()
    //Forth sentence
    textcomp.add(Neznaika.getName(2))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("BUTF").getLine)
    textcomp.add(Prepositions.valueOf("NOT").getLine)
    textcomp.add(Neznaika.doAction("WANTED"))
    textcomp.add(Neznaika.doAction("WAIT"))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("WHEN").getLine)
    textcomp.add(Klopi.getName(0))
    textcomp.add(Klopi.doAction("SATISFY"))
    textcomp.add(Pronouns.valueOf("HE").getName(1))
    textcomp.add(Neznaika.getBodyPart("BLOOD", 4))
    textcomp.next()
    //Fifth sentence
    textcomp.add(Conceptions.valueOf("TRUTH").getName(0))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("SOME").getLine)
    textcomp.add(Conceptions.valueOf("TIME").getName(0))
    textcomp.add(Pronouns.valueOf("HE").getName(0))
    textcomp.add(Pronouns.valueOf("HE").doAction("ENDURED"))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("BUTN").getLine)
    textcomp.add(Prepositions.valueOf("THEN").getLine)
    textcomp.add(Neznaika.doAction("SLIPPED"))
    textcomp.add(Prepositions.valueOf("ON").getLine)
    textcomp.add(Places.valueOf("FLOOR").getName(0))
    textcomp.add(Prepositions.valueOf("AND").getLine)
    textcomp.add(Neznaika.doAction("STARTED"))
    textcomp.add(Neznaika.doAction("THROWING"))
    textcomp.add(Prepositions.valueOf("THOSE").getLine)
    textcomp.add(Nasokomii.getName(3))
    textcomp.next()
    //Sixth sentence
    textcomp.add(Conceptions.valueOf("AIR").getName(0))
    textcomp.add(Places.valueOf("DOWN").getName(1))
    textcomp.add(Pronouns.valueOf("HE").doAction("HEWAS"))
    textcomp.add(Prepositions.valueOf("NOT").getLine)
    textcomp.add(Prepositions.valueOf("SUCH").getLine)
    textcomp.add(Conceptions.valueOf("STUFFY").getName(0))
    textcomp.add(",")
    textcomp.add(Prepositions.valueOf("THATS").getLine)
    textcomp.add(Neznaika.getName(0))
    textcomp.add(Neznaika.doAction("DECIDED"))
    textcomp.add(Neznaika.doAction("SIT"))
    textcomp.add(Prepositions.valueOf("ENTIRE").getLine)
    textcomp.add(Conceptions.valueOf("NIGHT").getName(0))
    textcomp.add(Prepositions.valueOf("ON").getLine)
    textcomp.add(Places.valueOf("FLOOR").getName(2))
    textcomp.next()
    
    textcomp.enableCommasSpaces(false)
    textcomp.enableDotsSpaces(false)
    textcomp.enableSOS(true)
    textcomp.enableEOS(true)
    textcomp.compile()
    println(textcomp.getResult)

    println("\n\n")

    println("Compilers.TextCompiler.hashCode(): " + textcomp.hashCode)
    println("Compilers.TextCompiler.equals(new Compilers.TextCompiler()): " + textcomp.equals(new TextCompiler()))
    println("Neznaika.equals(Stroptivii): " + Neznaika.equals(Stroptivii))
    println("Stroptivii.equals(Stroptivii): " + Stroptivii.equals(Stroptivii))

  }

}
