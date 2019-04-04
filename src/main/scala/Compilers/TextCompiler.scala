package Compilers

import java.util.ArrayList

class TextCompiler(val sentences: ArrayList[SentenceCompiler] = new ArrayList[SentenceCompiler]()) extends Compiler {
  private var finalSentence: StringBuilder = _

  if (sentences.isEmpty) this.sentences.add(new SentenceCompiler(newLine = false))

  def add(word: Word): Unit = word match {
    case Word(wordStr) =>
    this.sentences.get(this.size - 1).add(wordStr)
    if (word == ".") {
      this.sentences.add(new SentenceCompiler(newLine = true))
    }
  }


  def add(word: String): Unit = {
    this.sentences.get(this.size - 1).add(word)
    if (word == ".") {
      this.sentences.add(new SentenceCompiler(newLine = true))
    }
  }

  def next(): Unit = this.add(".")

  def getWordSizes: Array[Int] = {
    Range(0, this.sentences.size - 1).foldRight(Array[Int]())((i, b) => b ++ this.sentences.get(i).getWordSizes)
  }

  def size: Int = this.sentences.size

  def pop(index: Int): SentenceCompiler = {
    val out: SentenceCompiler = this.sentences.get(index)
    this.sentences.remove(index)
    out
  }

  def enableDotsSpaces(state: Boolean): Unit = this.sentences.forEach(_.enableDotsSpaces(state))

  def enableCommasSpaces(state: Boolean): Unit = this.sentences.forEach(_.enableCommasSpaces(state))

  def enableSOS(state: Boolean): Unit = this.sentences.forEach(_.enableSOS(state))

  def enableEOS(state: Boolean): Unit = this.sentences.forEach(_.enableEOS(state))

  def enableDots(state: Boolean): Unit = this.sentences.forEach(_.enableDots(state))

  def enableCommas(state: Boolean): Unit = this.sentences.forEach(_.enableCommas(state))

  def enableWords(state: Boolean): Unit = this.sentences.forEach(_.enableWords(state))

  def checkEmptyness(sentence: ArrayList[SentenceElem]): Boolean = sentence.size <= 3


  def compile(): Unit = {
    finalSentence = new StringBuilder()
    this.sentences.forEach(scomp => {
      scomp.compile()
      if (!checkEmptyness(scomp.getSentences)) {
        finalSentence.append(scomp.getResult)
      }
    })
  }

  def getResult: String = finalSentence.toString

  override def toString: String = {
    this.compile()
    this.getResult
  }


  override def hashCode: Int = {
    implicit def ternaryOp(bool: Boolean): Int = if (bool) -1 else 1

    var counter = this.sentences.size
    var summary = 0
    Range(0, this.sentences.size - 1).foreach { i =>
      val temp = this.sentences.get(i).hashCode
      summary = summary + this.sentences.get(i).size
      counter = counter + temp * (if (_: Boolean) 2 else -5)(i % 3 == 0) * (summary % 50 + 1)
    }

    counter * (summary % 2 == 0)
  }
}
