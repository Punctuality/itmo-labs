//import dao.db.{SlickOneClickOrderDao, SlickExecutorDao, SlickClientDao, SlickOrderDao}
//import dao.{OneClickOrderDao, ExecutorDao, ClientDao, OrderDao}
//import dao.inmemory.{InMemoryClientDao, InMemoryOrderDao, InMemoryOneClickOrderDao, InMemoryExecutorDao}
//import order.{OneClickOrderService, OrderService}
//import registration.client.ClientRegistrationService
//import registration.executor.ExecutorRegistrationService
//
//import scala.concurrent.ExecutionContext.Implicits.global
//
////case class A(x: String, y: Int)
////
////object B{
////  val a: (String, Int) => A = (A.apply _)
////  A.unapply
////}
//
//trait StudentsClubServices {
//
//  lazy val orderService: OrderService = {
//    new OrderService(clientDao, orderDao)
//  }
//
//  lazy val clientRegistrationService: ClientRegistrationService = {
//    new ClientRegistrationService(clientDao)
//  }
//
//  lazy val executorRegistrationService: ExecutorRegistrationService = {
//    new ExecutorRegistrationService(executorDao)
//  }
//
//  lazy val oneClickOrderService: OneClickOrderService = {
//    new OneClickOrderService(oneClickOrderDao)
//  }
//
//  protected def orderDao: OrderDao
//  protected def executorDao: ExecutorDao
//  protected def clientDao: ClientDao
//  protected def oneClickOrderDao: OneClickOrderDao
//}
//
//trait InMemoryStudentsClub extends StudentsClubServices {
//  override protected lazy val clientDao = new InMemoryClientDao
//  override protected lazy val executorDao = new InMemoryExecutorDao
//  override protected lazy val orderDao = new InMemoryOrderDao
//  override protected lazy val oneClickOrderDao = new InMemoryOneClickOrderDao(orderDao, clientDao)
//}
//
//trait SlickStudentsClub extends StudentsClubServices {
//  override protected lazy val orderDao: SlickOrderDao = new SlickOrderDao
//  override protected lazy val executorDao: SlickExecutorDao = new SlickExecutorDao
//  override protected lazy val clientDao: SlickClientDao = new SlickClientDao()
//  override protected lazy val oneClickOrderDao: SlickOneClickOrderDao = new SlickOneClickOrderDao()
//}