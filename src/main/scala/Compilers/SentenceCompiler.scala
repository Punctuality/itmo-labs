package Compilers

import java.util.ArrayList

class SentenceCompiler(val sentence: Array[String] = Array[String](), val newLine: Boolean = true) extends Compiler {
  private val sentences: java.util.ArrayList[SentenceElem] = new ArrayList[SentenceElem]()
  private var finalSentence: StringBuilder = _


  if (newLine) this.sentences.add(Word("\n"))

  this.sentences.add(new SOS())

  sentence.foreach(this.add)

  def getWordSizes: Array[Int] = Range(0, sentences.size - 1).map(sentences.get(_).getValue.length).toArray

  private def wordProcessor(word: String): SentenceElem = {
    var out: SentenceElem = null
    word match {
      case "," => out = new Comma()

      case "." => out = new Dot()

      case _ => out = Word(word)
    }
    out
  }


  def add(word: String): Unit = {
    this.sentences.add(wordProcessor(word))
  }

  def add(word: Word): Unit = word match { case Word(line) =>
    this.sentences.add(wordProcessor(line))
  }

  def size: Int = {
    this.sentences.size()
  }

  def pop(index: Int): SentenceElem = {
    val out: SentenceElem = this.sentences.get(index)
    this.sentences.remove(index)
    out
  }

  def enableDotsSpaces(state: Boolean): Unit = {
    val typeComparator: Class[_ <: Dot] = (new Dot).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setSpace(state)
    }
  }

  def enableCommasSpaces(state: Boolean): Unit = {
    val typeComparator: Class[_ <: Comma] = (new Comma).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setSpace(state)
    }
  }

  def enableSOS(state: Boolean): Unit = {
    val typeComparator: Class[_ <: SOS] = (new SOS).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  def enableEOS(state: Boolean): Unit = {
    val typeComparator: Class[_ <: EOS] = (new EOS).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  def enableDots(state: Boolean) {
    val typeComparator: Class[_ <: Dot] = (new Dot).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  def enableCommas(state: Boolean) {
    val typeComparator: Class[_ <: Comma] = (new Comma).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  def enableWords(state: Boolean) {
    val typeComparator: Class[_ <: Word] = new Word("Test").getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }


  def compile() {
    finalSentence = new StringBuilder()
    this.sentences.add(new EOS())
    this.sentences.forEach { elem =>
      finalSentence.append(elem.getValue)
    }
  }

  def getResult: String = {
    finalSentence.toString
  }

  def getSentences: ArrayList[SentenceElem] = {
    this.sentences
  }


  override def toString: String = {
    this.compile()
    this.getResult
  }


  override def hashCode: Int = {
    var counter: Int = this.sentences.size
    Range(0, this.sentences.size - 1).foreach { i =>
      this.sentences.get(i).getValue.toCharArray.foreach { chr =>
        val temp: Int = Character.getNumericValue(chr)
        counter += temp * (i % 3 + 1)
      }
    }
    counter
  }


  override def equals(obj: Any): Boolean = {
    val typeComparator = this.getClass
    if (obj.getClass != typeComparator) {
      false
    } else {
      obj.toString.equals(this.toString)
    }
  }
}


trait SentenceElem {
  def getValue: String

  def setSpace(space: Boolean): Unit

  def setVisible(visible: Boolean): Unit
}

class Comma extends SentenceElem {
  var value: String = ","
  var withSpace: Boolean = true
  var visible: Boolean = true

  def setVisible(visible: Boolean): Unit = {
    this.visible = visible
  }

  def setSpace(space: Boolean): Unit = {
    this.withSpace = space
  }

  def getValue: String = {
    var out = value
    if (this.withSpace) {
      out = " " + out
    }
    if (this.visible) {
      out
    } else {
      ""
    }
  }


  override def equals(obj: Any): Boolean = {
    val typeComparator = (new Comma).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }


  override def hashCode: Int = 90441
}


class Dot extends SentenceElem {
  var value: String = "."
  var withSpace: Boolean = true
  var visible: Boolean = true

  def setVisible(visible: Boolean): Unit = {
    this.visible = visible
  }

  def setSpace(space: Boolean): Unit = {
    this.withSpace = space
  }

  def getValue: String = {
    var out = value
    if (this.withSpace) {
      out = " " + out
    }
    if (this.visible) {
      out
    } else {
      ""
    }
  }

  override def equals(obj: Any): Boolean = {
    val typeComparator = (new Comma).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }

  override def hashCode: Int = 807807
}

class SOS extends SentenceElem {
  var value: String = "<Compilers.SOS>"
  var withSpace: Boolean = true
  var visible: Boolean = true

  def setVisible(visible: Boolean): Unit = {
    this.visible = visible
  }

  def setSpace(space: Boolean): Unit = {
    this.withSpace = space
  }

  def getValue: String = {
    var out = value
    if (this.withSpace) {
      out = " " + out
    }
    if (this.visible) {
      out
    } else {
      ""
    }
  }

  override def equals(obj: Any): Boolean = {
    val typeComparator = (new Comma).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }

  override def hashCode: Int = 505505
}

class EOS extends SentenceElem {
  var value: String = "<Compilers.EOS>"
  var withSpace: Boolean = true
  var visible: Boolean = true

  def setVisible(visible: Boolean): Unit = {
    this.visible = visible
  }

  def setSpace(space: Boolean): Unit = {
    this.withSpace = space
  }

  def getValue: String = {
    var out = value
    if (this.withSpace) {
      out = " " + out
    }
    if (this.visible) {
      out
    } else {
      ""
    }
  }

  override def equals(obj: Any): Boolean = {
    val typeComparator = (new Comma).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }

  override def hashCode: Int = 503503
}

case class Word(var value: String) extends SentenceElem {
  var withSpace: Boolean = true
  var visible: Boolean = true

//  if (value.isEmpty) throw new RuntimeException("String is empty!")

  def setVisible(visible: Boolean): Unit = {
    this.visible = visible
  }

  def setSpace(space: Boolean): Unit = {
    this.withSpace = space
  }

  def getValue: String = {
    var out = value
    if (this.withSpace) {
      out = " " + out
    }
    if (this.visible) {
      out
    } else {
      ""
    }
  }

  override def equals(obj: Any): Boolean = {
    val typeComparator = new Word("Test").getClass
    if (obj.getClass == typeComparator) {
      this.getValue.equals(obj.asInstanceOf[Word].getValue)
    } else {
      false
    }
  }


  override def toString: String = this.getValue

  override def hashCode: Int = this.getValue.hashCode
}
