//package dao.db
//
//import java.util.UUID
//
//import dao.ClientDao
//import domain.Client
//
//import scala.concurrent.{ExecutionContext, Future}
//
//
//class SlickClientDao(implicit ec: ExecutionContext) extends ClientDao {
//
//  import Schema.profile.api._
//  import Schema.{clients, db}
//
//  override def insert(client: Client): Future[Unit] =
//    db.run(clients += client).map(_ => ())
//
//  override def exists(clientId: UUID): Future[Boolean] = {
//    db.run(clients.filter(_.id === clientId).exists.result)
//  }
//
//
//}
