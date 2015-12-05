package main.scala.br.edu.ufabc.ufabcapi.server

import spray.http._
import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp
import spray.json.DefaultJsonProtocol
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import main.scala.br.edu.ufabc.ufabcapi.models._

/**
 * Classe usada para manipular dados JSON de disciplina
 */
case class DisciplinaObject (override val codigo: String, override val ano: Int, override val disciplina: String,
    override val categoria: String, override val creditos: Int, override val situacao: String,
    override val periodo: String, override val conceito: String ) extends Disciplina{
}


/**
 * Criado por: Caio Cezar de Melo - RA: 11102010
 * 
 * Esse metodo e responsavel por manter o servidor rodando. Ele recebe todos os requests HTTP e processa de acordo
 * com o caminho selecionado
 * 
 */

object DisciplinaProtocol extends DefaultJsonProtocol {
  implicit val disciplinaFormat = jsonFormat8(DisciplinaObject)
}

object ServerAPI extends App with SimpleRoutingApp {
  import DisciplinaProtocol._
  import spray.httpx.SprayJsonSupport._
  implicit val actorSystem = ActorSystem()
  
  startServer(interface = "localhost", port = 50501) {  
    println("O servidor foi iniciado")
      path("hello"){
        get {
        complete {
          "Welcome to the server"
        }
      }
    }~   
       path("test_end_point") {
         put {
        entity(as[Array[DisciplinaObject]]) { disciplinas =>
        disciplinas.foreach {println}
        complete(disciplinas.toString())
      }
    }
  }~
  path("retornacr"){
    post{
      entity(as[Array[DisciplinaObject]]) { disciplinas =>
        complete(RetornaCR(disciplinas).toString())
      }
    }
  }~
  path("retornaca"){
    post{
      entity(as[Array[DisciplinaObject]]) { disciplinas =>
        complete(RetornaCA(disciplinas).toString())
      }
    }
  }
  }
  
  /**
   * Entrada: Array de Disciplinas
   * Saida: Float com o CR do aluno
   * 
   * O metodo declara os inteiros relacionados ao conceitos e efetua a 
   * operacao de calculo de CR atraves da chamada do Calculo Unitario de CR
   */
  def RetornaCR(disciplinas: Array[DisciplinaObject]): Float ={
    println("Operacao de calcular o CR foi solicitada")
    
    var cr: Float = 0
    var creditosCursados: Int = 0
    
    for(dis <- disciplinas){
      println(dis.toString())
      cr += dis.CalculoUnitario()   
      creditosCursados += dis.creditos
    }
    
    cr /= creditosCursados
    
    cr
  }
  
   /**
   * Entrada: Array de Disciplinas
   * Saida: Float com o CA do aluno
   * 
   * O metodo declara os inteiros relacionados ao conceitos e efetua a 
   * operacao de calculo de CA apenas das disciplinas em que foi aprovado atraves da chamada do CalculoUnitario
   */
  def RetornaCA(disciplinas: Array[DisciplinaObject]): Float ={
    println("Operacao de calcular o CA foi solicitada")
    
    var ca: Float = 0
    var creditosCursados: Int = 0
    
    for(dis <- disciplinas){
      dis.situacao match{
        case "Aprovado" => {
          ca += dis.CalculoUnitario()
          creditosCursados += dis.creditos
        }
        case "Reprovado" => {}
        case "Repr.Freq" => {}
      }
    }
    
    ca /= creditosCursados
    
    ca
  }
  
  def RetornaCSV(disciplinas: Array[DisciplinaObject]): String = {
    println("Operacao de retonar CSV foi solicitada")
    var teste: String = "" 

    for(dis <- disciplinas){
      StringBuilder sb = new StrinbBuilder();
      sb.
    }
     teste.addString(dis.toString())
   
  }
  


}