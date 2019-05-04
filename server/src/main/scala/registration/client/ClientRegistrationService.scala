//package registration.client
//
//import java.util.UUID
//
//import dao.ClientDao
//import domain.Client
//
//import scala.concurrent.{Future, ExecutionContext}
//
//class ClientRegistrationService(clientDao: ClientDao)
//                               (implicit ec: ExecutionContext) {
//
//  def register(name: String, phone: String): Future[Client] = {
//    val client = Client(UUID.randomUUID(), name, phone)
//
//    clientDao
//      .insert(client)
//      .map(_ => client)
//  }
//}
