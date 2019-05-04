//package dao.inmemory
//
//import dao.OneClickOrderDao
//import domain.{Client, Order}
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//class InMemoryOneClickOrderDao(inMemoryOrderDao: InMemoryOrderDao,
//                               inMemoryClientDao: InMemoryClientDao) extends OneClickOrderDao {
//  override def insertAll(client: Client, order: Order): Future[Unit] = {
//    for {
//      _ <- inMemoryClientDao.insert(client)
//      _ <- inMemoryOrderDao.insert(order)
//    } yield ()
//  }
//}
