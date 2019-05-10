package dao.db

import java.util.UUID

import dao.WordDao
import domain.Word

import scala.concurrent.{ExecutionContext, Future}

class SlickWordDao(implicit ec: ExecutionContext) extends WordDao {

  import Schema.profile.api._
  import Schema.{db, words}

  override def insert(word: Word): Future[Unit] =
    db.run(words += word).map(_ => ())

  override def insert(newWords: Seq[Word]): Future[Unit] =
    db.run(words ++= newWords).map(_ => ())

  override def find(userId: UUID): Future[Option[Seq[Word]]] =
    db.run(words.filter(_.userId === userId).result).map(seq => Option(seq).filter(_.nonEmpty))

  override def change(word: Word): Future[Unit] =
    db.run(words.filter(_.id === word.id)
      .update(word)).map(_ => ())

  override def delete(userId: UUID, position: Int): Future[Unit] =
    db.run(words.filter(_.userId === userId).filter(_.position === position).delete)
      .map(_ => ())

  override def compile(userId: UUID): Future[String] =
    db.run(words.filter(_.userId === userId).result)
      .map { seq =>
        val tempList: Seq[Word] = seq.sortBy(_.position)
        val finalSentence = new StringBuilder()
        tempList.foreach { elem =>
          finalSentence.append((if (elem.visible) elem.value else "") + (if (elem.withSpace) " " else ""))
        }
        finalSentence.toString
      }
}
