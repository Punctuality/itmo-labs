//package order
//
//import java.util.UUID
//
//import dao.OneClickOrderDao
//import dao.OneClickOrderDao
//import domain.{Client, Order}
//
//import concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
//// Создает заказ в "один клик клиента" без необходимости отдельно регистрировать заказчика и исполнителя
//
//class OneClickOrderService(oneClickOrderDao: OneClickOrderDao) {
//  def createOneClick(clientName: String,
//                     clientPhone: String,
//                     orderDescription: String,
//                     orderPrice: BigDecimal): Future[Order] = {
//    val client = Client(UUID.randomUUID(), clientName, clientPhone)
//    val order = Order(UUID.randomUUID(), orderDescription, orderPrice, client.id, None)
//
//    oneClickOrderDao.insertAll(client, order)
//      .map(_ => order)
//  }
//}
