//package dao.db
//
//import dao.OneClickOrderDao
//import domain.{Client, Order}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class SlickOneClickOrderDao extends OneClickOrderDao {
//
//  import Schema.profile.api._
//  import Schema.{clients, db, orders}
//
//  override def insertAll(client: Client, order: Order): Future[Unit] =
//    db.run((clients += client) andThen (orders += order) transactionally).map(_ => ())
//
//}