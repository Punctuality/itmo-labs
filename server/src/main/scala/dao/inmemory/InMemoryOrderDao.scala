//package dao.inmemory
//
//import java.util.UUID
//import java.util.concurrent.ConcurrentHashMap
//
//import dao.OrderDao
//import domain.Order
//
//import scala.concurrent.Future
//import scala.collection.JavaConverters._
//
//class InMemoryOrderDao extends OrderDao {
//  override def sumByClients(): Future[Map[UUID, BigDecimal]] = {
//    Future.successful(
//    orders.asScala
//      .values
//      .groupBy(_.clientId)
//      .mapValues(_.map(_.price).sum)
//    )
//  }
//
//  override def insert(order: Order): Future[Unit] = {
//    Future.successful(
//      orders.put(order.id, order)
//    )
//  }
//
//  override def find(id: UUID): Future[Option[Order]] = {
//    Future.successful(
//      orders.asScala.get(id)
//    )
//  }
//
//  override def list(clientId: UUID): Future[Seq[Order]] = {
//    Future.successful(
//      orders.asScala
//        .values
//        .filter(_.clientId == clientId)
//        .toSeq
//    )
//  }
//
//  override def assign(orderId: UUID, executorId: UUID): Future[Unit] = {
//    Future.successful(
//      orders
//        .computeIfPresent(orderId, (id, order) => order.copy(executorId = Some(executorId)))
//    )
//  }
//
//
//  private val orders = new ConcurrentHashMap[UUID, Order]()
//}
