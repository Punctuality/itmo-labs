package dao.db

import java.time.LocalDateTime
import java.util.UUID

import domain.Word
import org.postgresql.util.PSQLException
import slick.jdbc
import slick.jdbc._
import slick.lifted.ProvenShape
import users.User

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Schema {

  import slick.jdbc.PostgresProfile.api._

  implicit val uuidMapper: BaseColumnType[String] = MappedColumnType.base[String, UUID](UUID.fromString, _.toString)

  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    def id: Rep[UUID] = column("ID", O.PrimaryKey)

    def passwordHash: Rep[String] = column[String]("PASSWORD")

    def name: Rep[Option[String]] = column[Option[String]]("NAME")

    def email: Rep[Option[String]] = column[Option[String]]("EMAIL")

    override def * : ProvenShape[User] = (id, passwordHash, name, email) <> ((User.apply _).tupled, User.unapply _)
  }

  val users = TableQuery[Users]

  class Words(tag: Tag) extends Table[Word](tag, "WORDS") {

    def id: Rep[UUID] = column("ID", O.PrimaryKey)

    def creationDate: Rep[LocalDateTime] = column[LocalDateTime]("DATE")

    def position: Rep[Int] = column[Int]("POSITION")

    def value: Rep[String] = column[String]("VALUE")

    def space: Rep[Boolean] = column[Boolean]("SPACE")

    def visible: Rep[Boolean] = column[Boolean]("VISIBLE")

    def userId: Rep[UUID] = column[UUID]("USER")

    override def * : ProvenShape[Word] = (id, creationDate, position, value, space, visible, userId) <> ((Word.apply _).tupled, Word.unapply _)
  }

  val words = TableQuery[Words]

  def setup(): Future[Unit] = db.run((
    Schema.users.schema ++ Schema.words.schema
    ).create).recover{
    case sqlExp: PSQLException => println(sqlExp.getMessage)
  }

  val profile: PostgresProfile = slick.jdbc.PostgresProfile

  val db: jdbc.PostgresProfile.backend.Database = Database.forConfig("postgres")

  val tables = List(users, words)


  type IO[+R, -E <: Effect] = DBIOAction[R, NoStream, E]
}