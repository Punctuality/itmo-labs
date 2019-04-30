package Compilers

import java.time.LocalDateTime
import java.util
import java.util.{Collections, Comparator}

class SentenceCompiler(val sentence: Array[String] = Array[String](), val newLine: Boolean = true) extends Compiler {
  private val sentences: util.List[SentenceElem] =
    Collections.synchronizedList(new util.ArrayList[SentenceElem])
//    new util.ArrayList[SentenceElem]()
  private var finalSentence: StringBuilder = _

  private val nameCompr: Comparator[SentenceElem] =
    (o1: SentenceElem, o2: SentenceElem) => o1.getValue.compareTo(o2.getValue)
  private val positionCompr: Comparator[SentenceElem] =
    (o1: SentenceElem, o2: SentenceElem) => o1.getPosition.compareTo(o2.getPosition)


  if (newLine) this.sentences.add(Word("\n")(0))

  sentence.foreach(this.add)

  def getWordSizes: Array[Int] = Range(0, sentences.size - 1).map(sentences.get(_).getValue.length).toArray

  private def wordProcessor(word: String): SentenceElem = {
    var out: SentenceElem = null
    word match {
      case "," => out = new Comma(this.sentences.size())

      case "." => out = new Dot(this.sentences.size())

      case _ => out = Word(word)(this.sentences.size())
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
    Range(index+1, this.sentences.size()-1).foreach{ind =>
      this.sentences.get(ind).setPosition(this.sentences.get(ind).getPosition-1)}
    out
  }

  def enableDotsSpaces(state: Boolean): Unit = {
    val typeComparator: Class[_ <: Dot] = new Dot(0).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setSpace(state)
    }
  }

  def enableCommasSpaces(state: Boolean): Unit = {
    val typeComparator: Class[_ <: Comma] = new Comma(0).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setSpace(state)
    }
  }

  @deprecated
  def enableSOS(state: Boolean): Unit = {
    val typeComparator: Class[_ <: SOS] = new SOS(0).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  @deprecated
  def enableEOS(state: Boolean): Unit = {
    val typeComparator: Class[_ <: EOS] = new EOS(0).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  def enableDots(state: Boolean): Unit = {
    val typeComparator: Class[_ <: Dot] = new Dot(0).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  def enableCommas(state: Boolean): Unit = {
    val typeComparator: Class[_ <: Comma] = new Comma(0).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }

  def enableWords(state: Boolean): Unit = {
    val typeComparator: Class[_ <: Word] = Word("")(0).getClass
    Range(0, this.sentences.size - 1).filter(this.sentences.get(_).getClass == typeComparator).foreach {
      this.sentences.get(_).setVisible(state)
    }
  }


  def compile(): Unit = {
    this.sentences.sort(nameCompr)
    val tempList: util.List[SentenceElem] = this.sentences
    tempList.sort(positionCompr)
    finalSentence = new StringBuilder()
    tempList.forEach { elem =>
      finalSentence.append(elem.getValue)
    }
  }

  def getResult: String = {
    finalSentence.toString
  }

  def getSentences: util.List[SentenceElem] = {
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


trait SentenceElem{
  private val cdt: LocalDateTime = LocalDateTime.now

  def getCreationDate: LocalDateTime = cdt

  def getPosition: Int

  def setPosition(i: Int): Unit

  def getValue: String

  def setSpace(space: Boolean): Unit

  def setVisible(visible: Boolean): Unit
}

class Comma(var position: Int) extends SentenceElem {
  var value: String = ","
  var withSpace: Boolean = true
  var visible: Boolean = true

  override def getPosition: Int = position

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
    val typeComparator = new Comma(0).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }


  override def hashCode: Int = 90441

  override def setPosition(i: Int): Unit =
    position = i
}


class Dot(var position: Int) extends SentenceElem {
  var value: String = "."
  var withSpace: Boolean = true
  var visible: Boolean = true

  override def getPosition: Int = position

  override def setPosition(i: Int): Unit =
    position = i

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
    val typeComparator = new Comma(0).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }

  override def hashCode: Int = 807807
}

@deprecated
class SOS(var position: Int) extends SentenceElem {
  var value: String = "<Compilers.SOS>"
  var withSpace: Boolean = true
  var visible: Boolean = true

  override def getPosition: Int = position

  override def setPosition(i: Int): Unit =
    position = i

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
    val typeComparator = new SOS(0).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }

  override def hashCode: Int = 505505
}

@deprecated
class EOS(var position: Int) extends SentenceElem {
  var value: String = "<Compilers.EOS>"
  var withSpace: Boolean = true
  var visible: Boolean = true

  override def getPosition: Int = position

  override def setPosition(i: Int): Unit =
    position = i

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
    val typeComparator = new EOS(0).getClass
    obj.getClass == typeComparator
  }


  override def toString: String = {
    this.getValue
  }

  override def hashCode: Int = 503503
}

case class Word(var value: String)(var position: Int) extends SentenceElem {
  var withSpace: Boolean = true
  var visible: Boolean = true

//  if (value.isEmpty) throw new RuntimeException("String is empty!")

  override def setPosition(i: Int): Unit =
    position = i

  override def getPosition: Int = position

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
    val typeComparator = Word("")(0).getClass
    if (obj.getClass == typeComparator) {
      this.getValue.equals(obj.asInstanceOf[Word].getValue)
    } else {
      false
    }
  }


  override def toString: String = this.getValue

  override def hashCode: Int = this.getValue.hashCode
}

