package dao.inmemory

import java.util
import java.util.{Comparator, UUID}

import dao.WordDao
import domain._

import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}

class InMemoryWordDao(implicit ec: ExecutionContext) extends WordDao {
  private val wordsTree: TrieMap[UUID, Vector[Word]] = new TrieMap[UUID, Vector[Word]]()
  private var finalSentence: StringBuilder = _

//  private val nameCompr: Comparator[Word] =
//    (o1: Word, o2: Word) => o1.value.compareTo(o2.value)
//  private val positionCompr: Comparator[Word] =
//    (o1: Word, o2: Word) => o1.position.compareTo(o2.position)

  override def insert(word: Word): Future[Unit] =
    Future(this.wordsTree.update(word.userId,
      this.wordsTree.getOrElse(word.userId, Vector[Word]()) :+ word
    )
    )

  override def insert(words: Seq[Word]): Future[Unit] =
    Future.traverse(words)(this.insert).map(_ => ())

  override def find(userId: UUID): Future[Option[Seq[Word]]] =
    Future(this.wordsTree.get(userId))

  override def change(word: Word): Future[Unit] =
    Future(this.wordsTree.update(word.userId,
      this.wordsTree.get(word.userId) match {
        case Some(words) => words.map(itWord => if (word.id == itWord.id) word else itWord)
        case None => throw new RuntimeException("No such user!")
      }
    )
    )

  override def delete(userId: UUID, position: Int): Future[Unit] =
    Future(this.wordsTree.update(userId,
      this.wordsTree.get(userId) match {
        case Some(words) => words.filter(word => word.position != position)
        case None => throw new RuntimeException("No such user!")
      }
    )
    )

    def compile(userId: UUID): Unit = {
      val tempList: Vector[Word] = this.wordsTree.getOrElse(userId, Vector[Word]()).sortBy(_.position)
      finalSentence = new StringBuilder()
      tempList.foreach{ elem =>
        finalSentence.append(elem.value)
      }
    }

  //  def getWordSizes: Array[Int] = Range(0, sentences.size - 1).map(sentences.get(_).value.length).toArray
  //
  //  private def wordProcessor(word: String): Word =
  //    Word(word, this.sentences.size())
  //
  //
  //  def add(word: String): Unit = {
  //    this.sentences.add(wordProcessor(word))
  //  }
  //
  //  def add(word: Word): Unit = word match { case Word(line) =>
  //    this.sentences.add(wordProcessor(line))
  //  }
  //
  //  def size: Int = {
  //    this.sentences.size()
  //  }
  //
  //  def pop(index: Int): Word = {
  //    val out: Word = this.sentences.get(index)
  //    this.sentences.remove(index)
  //    Range(index+1, this.sentences.size()-1).foreach{ind =>
  //      this.sentences.get(ind).setPosition(this.sentences.get(ind).getPosition-1)}
  //    out
  //  }
  //
  //  def enableWords(state: Boolean): Unit = {
  //    Range(0, this.sentences.size - 1).foreach {
  //      this.sentences.get(_).setVisible(state)
  //    }
  //  }
  //
  //
  //
  //  def getResult: String = {
  //    finalSentence.toString
  //  }
  //
  //  def getSentences: util.List[Word] = {
  //    this.sentences
  //  }
  //
  //
}