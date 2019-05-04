//package ru.tinkoff.studentclub.order
//
//import java.util.UUID
//
//import dao.{ClientDao, OrderDao}
//import domain.Order
//
//import scala.concurrent.{Future, ExecutionContext}
//
//class OrderService(clientDao: ClientDao,
//                   orderDao: OrderDao)
//                  (implicit ec: ExecutionContext) {
//  def create(description: String, price: BigDecimal, clientId: UUID): Future[Order] = {
//    for {
//      clientExists <- clientDao.exists(clientId)
//      _ = if (!clientExists) throw new IllegalArgumentException("Client does not exists")
//      order = Order(UUID.randomUUID(), description, price, clientId, None)
//      _ <- orderDao.insert(order)
//    } yield order
//  }
//
//  def find(orderId: UUID): Future[Option[Order]] =
//    orderDao.find(orderId)
//
//  def listByClient(clientId: UUID): Future[Seq[Order]] =
//    orderDao.list(clientId)
//
//  def assign(orderId: UUID, executorId: UUID): Future[Unit] =
//    orderDao.assign(orderId, executorId)
//
//  def sumByClients(): Future[Map[UUID, BigDecimal]] =
//    orderDao.sumByClients()
//}
