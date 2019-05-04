//package dao.db
//
//import java.util.UUID
//
//import dao.OrderDao
//import domain.Order
//import slick.dbio.Effect.Read
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class SlickOrderDao(implicit ec: ExecutionContext) extends OrderDao {
//  import Schema._
//  import Schema.profile.api._
//
//  override def sumByClients(): Future[Map[UUID, BigDecimal]] = {
//    db.run {
//      val orderStats: IO[Seq[(UUID, Option[BigDecimal])], Read] =
//        orders
//          .groupBy(_.clientId)
//          .map { case (id, query) => (id, query.map(_.price).sum) }
//          .result
//
//      orderStats
//        .map(_.foldLeft(Map[UUID, BigDecimal]()){ case (acc, (id, priceOpt)) =>
//            val newTotal = acc.getOrElse(id, BigDecimal(0)) + priceOpt.getOrElse(0)
//            acc.updated(id, newTotal)
//        })
//    }
//  }
//
//  override def insert(order: Order): Future[Unit] =
//    db.run(orders += order).map(_ => ())
//
//  override def find(id: UUID): Future[Option[Order]] =
//    db.run(orders.filter(_.id === id).result.headOption)
//
//  override def list(clientId: UUID): Future[Seq[Order]] =
//    db.run(orders.filter(_.clientId === clientId).result)
//
//  override def assign(orderId: UUID, executorId: UUID): Future[Unit] =
//    db.run(orders.filter(_.id === orderId).map(_.executorId).update(Some(executorId))).map(_ => ())
//}
