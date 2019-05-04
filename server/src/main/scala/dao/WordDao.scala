package dao

import java.util.UUID

import domain.Word

import scala.concurrent.Future

trait WordDao {
  def insert(word: Word): Future[Unit]

  def insert(words: Seq[Word]): Future[Unit]

  def find(userId: UUID): Future[Option[Seq[Word]]]

  def change(word: Word): Future[Unit]

  def delete(userId: UUID, position: Int): Future[Unit]
}
