package dao.inmemory

import java.util.UUID

import dao.UserDao
import users.User

import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}

class InMemoryUserDao(implicit ec: ExecutionContext) extends UserDao {
  private val usersMap: TrieMap[UUID, User] = new TrieMap[UUID, User]()

  override def insert(user: User): Future[Unit] =
    Future(this.usersMap update(user.id, user))

  override def find(id: UUID): Future[Option[User]] =
    Future(this.usersMap get id)

  override def list: Future[Seq[User]] =
    Future(this.usersMap.toSeq map (_._2))

  override def change(user: User): Future[Unit] =
    Future(this.usersMap.update(user.id,
      this.usersMap.get(user.id) match {
        case Some(_) => user
        case None => throw new RuntimeException("No such user!")
      }
    )
    )
}
