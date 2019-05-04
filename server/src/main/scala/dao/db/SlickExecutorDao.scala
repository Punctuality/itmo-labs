//package dao.db
//
//import dao.ExecutorDao
//import domain.Executor
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class SlickExecutorDao(implicit ec: ExecutionContext) extends ExecutorDao {
//
//  import Schema.profile.api._
//  import Schema.{db, executors}
//
//  override def insert(executor: Executor): Future[Unit] = db.run(executors += executor).map(_ => ())
//}
