//package dao.inmemory
//
//import java.util.UUID
//import java.util.concurrent.ConcurrentHashMap
//
//import dao.ClientDao
//import domain.Client
//
//import scala.concurrent.Future
//
//class InMemoryClientDao extends ClientDao {
//  override def insert(client: Client): Future[Unit] = {
//    Future.successful(
//      clients.put(client.id, client)
//    )
//  }
//
//  override def exists(clientId: UUID): Future[Boolean] = {
//    Future.successful(
//      clients.containsKey(clientId)
//    )
//  }
//
//
//  private val clients = new ConcurrentHashMap[UUID, Client]()
//}
