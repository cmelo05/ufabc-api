package main.scala.br.edu.ufabc.ufabcapi.server

import spray.http._
import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp
import spray.json.DefaultJsonProtocol
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._

/**
 * Created by: Caio Cezar de Melo - RA: 11102010
 * 
 * This method is responsible for keeping the server running. It receives all the HTTP requests and process
 * them according to the path selected.
 */

case class Disciplina (val codigo: String, val ano: Int, val disciplina: String, val categoria: String,
                  val creditos: Int, val situacao: String, val periodo: String, val conceito: String ) {
  
  /**
 	* Override to return a CSV model
 	*/
  override def toString(): String = codigo+";"+ano.toString()+";"+disciplina+";"+categoria+
  ";"+creditos.toString()+";"+situacao+";"+periodo+";"+conceito     
}

object DisciplinaProtocol extends DefaultJsonProtocol {
  implicit val disciplinaFormat = jsonFormat8(Disciplina)
}

object ServerAPI extends App with SimpleRoutingApp {
  import DisciplinaProtocol._
  import spray.httpx.SprayJsonSupport._
  implicit val actorSystem = ActorSystem()
  
  startServer(interface = "localhost", port = 50501) {   
      path("hello"){
        get {
        complete {
          "Welcome to the server"
        }
      }
    }~   
       path("test_end_point") {
         put {
        entity(as[Array[Disciplina]]) { disciplinas =>
        disciplinas.foreach {println}
        complete(disciplinas.toString())
      }
    }
  }
  }

}