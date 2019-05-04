package jdbc

import java.sql.{Connection, DriverManager, ResultSet, Statement}

/**
  * A Scala JDBC connection example by Alvin Alexander,
  * https://alvinalexander.com
  */
object ScalaJdbcConnectSelect {

  def main(args: Array[String]) {
    // connect to the database named "mysql" on the localhost
    val driver = "org.postgresql.Driver"
    val url = "jdbc:postgresql://localhost/postgres"
    val username = "postgres"
    val password = "tf270207"

    // there's probably a better way to do this
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement: Statement = connection.createStatement()
      val resultSet: ResultSet = statement.executeQuery("SELECT host, user FROM user")
      while ( resultSet.next() ) {
        val host = resultSet.getString("host")
        val user = resultSet.getString("user")
        println("host, user = " + host + ", " + user)
      }
    } catch {
      case e => e.printStackTrace
    }
    connection.close()
  }

}