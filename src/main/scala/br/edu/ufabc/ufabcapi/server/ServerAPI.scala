package main.scala.br.edu.ufabc.ufabcapi.server

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

/**
 * Created by: Caio Cezar de Melo - RA: 11102010
 * 
 * This method is responsible for keeping the server running. It receives all the HTTP requests and process
 * them according to the path selected.
 */
object ServerAPI extends App with SimpleRoutingApp {
  implicit val actorSystem = ActorSystem()
  
  startServer(interface = "localhost", port = 8080) {
    get {
      path("hello"){
        complete {
          "Welcome to the server"
        }
      }
    }
  }

}