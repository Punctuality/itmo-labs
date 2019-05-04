//package registration.executor
//
//import java.util.UUID
//
//import dao.ExecutorDao
//import domain.Executor
//
//import scala.concurrent.{Future, ExecutionContext}
//
//class ExecutorRegistrationService(executorDao: ExecutorDao)
//                                 (implicit ec: ExecutionContext) {
//
//  def register(name: String, phone: String): Future[Executor] = {
//    val executor = Executor(UUID.randomUUID(), name, phone)
//    executorDao
//      .insert(executor)
//      .map(_ => executor)
//  }
//}
