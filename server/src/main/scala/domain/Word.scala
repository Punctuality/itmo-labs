package domain

import java.time.LocalDateTime
import java.util.UUID


case class Word(id: UUID = UUID.randomUUID,
                dateTime: LocalDateTime = LocalDateTime.now,
                var position: Int,
                value: String,
                var withSpace: Boolean = true,
                var visible: Boolean = true,
                userId: UUID) {

  override def toString: String = this.value

  override def hashCode: Int = this.value.hashCode + this.position.hashCode + userId.hashCode
}


object Word {
  def makeWord(word: WordParseTemplate, position: Int, userId: UUID): Word =
  makeWord(word.s, position, userId)

  def makeWord(word: String, position: Int, userId: UUID): Word =
  Word(position = position, value = word, userId = userId)

  def makeWords(words: Seq[String], startPosition: Int, userId: UUID): Seq[Word] =
  words.zipWithIndex.map(pair => makeWord(pair._1, pair._2 + startPosition, userId))
}

case class WordParseTemplate(s: String)
