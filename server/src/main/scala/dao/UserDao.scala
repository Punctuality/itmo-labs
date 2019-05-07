package dao

import java.util.UUID

import users.User

import scala.concurrent.Future

trait UserDao {
  def insert(user: User): Future[Unit]

  def find(id: UUID): Future[Option[User]]

  def list: Future[Seq[User]]

  def  change(user: User): Future[Unit]
}
