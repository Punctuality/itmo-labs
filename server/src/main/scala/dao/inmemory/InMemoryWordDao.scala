package dao.inmemory

import java.util.UUID

import dao.WordDao
import domain._

import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}

class InMemoryWordDao(implicit ec: ExecutionContext) extends WordDao {
  private val wordsMap: TrieMap[UUID, Vector[Word]] = new TrieMap[UUID, Vector[Word]]()
  private var finalSentence: StringBuilder = _

  //  private val nameCompr: Comparator[Word] =
  //    (o1: Word, o2: Word) => o1.value.compareTo(o2.value)
  //  private val positionCompr: Comparator[Word] =
  //    (o1: Word, o2: Word) => o1.position.compareTo(o2.position)

  override def insert(word: Word): Future[Unit] =
    Future(this.wordsMap.update(word.userId,
      this.wordsMap.getOrElse(word.userId, Vector[Word]()) :+ word
    )
    )

  override def insert(words: Seq[Word]): Future[Unit] =
    Future.traverse(words)(this.insert).map(_ => ())

  override def find(userId: UUID): Future[Option[Seq[Word]]] =
    Future(this.wordsMap.get(userId))

  override def change(word: Word): Future[Unit] =
    Future(this.wordsMap.update(word.userId,
      this.wordsMap.get(word.userId) match {
        case Some(words) => words.map(itWord => if (word.id == itWord.id) word else itWord)
        case None => throw new RuntimeException("No such user!")
      }
    )
    )

  override def delete(userId: UUID, position: Int): Future[Unit] =
    Future(this.wordsMap.update(userId,
      this.wordsMap.get(userId) match {
        case Some(words) => words.filter(word => word.position != position)
        case None => throw new RuntimeException("No such user!")
      }
    )
    )

  override def compile(userId: UUID): Future[String] = Future{
    val tempList: Vector[Word] = this.wordsMap.getOrElse(userId, Vector[Word]()).sortBy(_.position)
    finalSentence = new StringBuilder()
    tempList.foreach { elem =>
      finalSentence.append((if (elem.visible) elem.value else "") + (if (elem.withSpace) " " else ""))
    }
    finalSentence.toString
  }

}