//package dao.inmemory
//
//import java.util.UUID
//import java.util.concurrent.ConcurrentHashMap
//
//import dao.ExecutorDao
//import domain.Executor
//
//import scala.concurrent.Future
//
//class InMemoryExecutorDao extends ExecutorDao {
//  override def insert(executor: Executor): Future[Unit] =
//    Future.successful(
//      executors.put(executor.id, executor)
//    )
//
//  private val executors = new ConcurrentHashMap[UUID, Executor]()
//}
