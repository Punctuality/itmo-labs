package dao.db

import java.util.UUID

import dao.UserDao
import users.User

import scala.concurrent.{ExecutionContext, Future}

class SlickUserDao(implicit ec: ExecutionContext) extends UserDao {
  import Schema.profile.api._
  import Schema.{db, users}

  override def insert(user: User): Future[Unit] =
    db.run(users += user).map(_ => ())

  override def find(id: UUID): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  override def list: Future[Seq[User]] =
    db.run(users.result)

  override def change(user: User): Future[Unit] =
    db.run(users.filter(_.id === user.id)
      .update(user)).map(_ => ())

  def show: Future[Seq[String]] =
    db.run(users.result).map(seq => seq.map(_.toString))

}
