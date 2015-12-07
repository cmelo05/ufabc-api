package main.scala.br.edu.ufabc.ufabcapi.server

import spray.http._
import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp
import spray.json.DefaultJsonProtocol
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import main.scala.br.edu.ufabc.ufabcapi.models._
import scala.io.Source._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write

/**
 * Classe usada para manipular dados JSON de disciplina
 */
case class DisciplinaObject (override val codigo: String, override val ano: Int, override val disciplina: String,
    override val categoria: String, override val creditos: Int, override val situacao: Option[String],
    override val periodo: String, override val conceito: String ) extends Disciplina{
}

case class DisciplinaShort(val codigo: String, val nome: String, val creditos: Int)

/**
 * Criado por: Caio Cezar de Melo - RA: 11102010
 * 
 * Esse metodo e responsavel por manter o servidor rodando. Ele recebe todos os requests HTTP e processa de acordo
 * com o caminho selecionado
 * 
 */

object DisciplinaProtocol extends DefaultJsonProtocol {
  implicit val disciplinaFormat = jsonFormat8(DisciplinaObject)
  implicit val disciplinaShortFormat = jsonFormat3(DisciplinaShort)
}

object ServerAPI extends App with SimpleRoutingApp {
  import DisciplinaProtocol._
  import spray.httpx.SprayJsonSupport._
  implicit val actorSystem = ActorSystem()
  
  startServer(interface = "localhost", port = 50501) {  
    println("O servidor foi iniciado")
    /**
     * Esta diretiva recebe uma solicitacao post no caminho retona / String com um JSON de disciplinas e retona
     * o CR ou o CA
     */
    post{
      path("retorna" / Segment) {
        tipo => {
          entity(as[Array[DisciplinaObject]]) { disciplinas =>
            tipo.toUpperCase() match {
              case "CR" => complete(RetornaCR(disciplinas).toString())
              case "CA" => complete(RetornaCA(disciplinas).toString())
            }
          }
        }
      }~
      path("retorna" / "quantidade" / Segment){
        conceito => {
          entity(as[Array[DisciplinaObject]]) { disciplinas =>
            {
              complete(RetornaQuantidadeConceito(disciplinas,conceito).toString())
            }
          }
        }
      }~
      path("insere" / "grade" / Segment){
        curso => {
          entity(as[Array[DisciplinaShort]]) { disciplinas =>
            {
              InsereGrade(disciplinas,curso) match {
                case true => complete(curso + " adicionado com sucesso")
                case false => complete(curso + " não foi inserido")
              }
            }
          }
        }
      }
    }~
    get{
      path("retorna" / Segment / IntNumber) {
        (tipo, ano) => {
          tipo.toUpperCase() match{
            case "DISCIPLINAS" => {
              respondWithMediaType(MediaTypes.`application/json`)
              complete(RetornaDisciplinaAno(ano))
            }
          }
        }
      }~
      path("retorna" / Segment / IntNumber / Segment) {
        (tipo, ano, codigo) => {
          tipo.toUpperCase() match{
            case "DISCIPLINAS" => {
              respondWithMediaType(MediaTypes.`application/json`)
              complete(RetonarDeterminadaDisciplinaAno(ano,codigo))
            }
          }
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
        case Some(d) => {
          d match {
            case "Aprovado" => {
              ca += dis.CalculoUnitario()
              creditosCursados += dis.creditos
            }
            case "Reprovado" => {}
            case "Repr.Freq" => {}
           }
         }
        case None => {}
      }
    }
    
    ca /= creditosCursados
    
    ca
  }
  
  /**
   * Entrada: ano a ser procurado nas Resources
   * Saida: JSON formatado com os dados das disciplinas
   */
  def RetornaDisciplinaAno(ano: Int): String = {
    val arq = "grade"+ano.toString()+".json"
    pretty(render(LerArquivo(arq)))
  }
  
  /**
   * Entrada: ano e codigo
   * Saida: String formatada com o JSON
   * 
   * Esse metodo recebe um ano e um codigo e filtra todas as disciplinas daquela
   * matriz que tenham o codigo recebido
   */
  def RetonarDeterminadaDisciplinaAno(ano: Int, codigo: String): String = {
    implicit val formats = DefaultFormats
    
    val arq = "grade"+ano.toString()+".json"
    
    val objetos = (LerArquivo(arq)).extract[List[DisciplinaShort]]
    
    val disciplina = objetos.filter(_.codigo == codigo)
    
    write(disciplina)
  }


  /**
   * Entrada: Nome de um Arquivo
   * Saida: Objeto do tipo JValue
   * 
   * Esse metodo recebe o nome de um arquivo e faz o parse da String de retorno
   */
def LerArquivo(nomeDoArquivo: String): JValue = {
    val path = this.getClass.getResource(nomeDoArquivo).toURI()
  
    val arquivoLido = scala.io.Source.fromFile(path)("UTF-8").getLines().mkString
    
    parse(arquivoLido)
}

def RetornaQuantidadeConceito(disciplinas: Array[DisciplinaObject], conceito: String): Int = {
  val disciplinasFiltro = disciplinas.filter { dis => dis.conceito.equals(conceito) }
  
  disciplinasFiltro.size  
}

/**
 * Not working at the moment
 */
def InsereGrade(disciplinas: Array[DisciplinaShort], nome: String): Boolean = {
  import java.io._
  implicit val formats = DefaultFormats

  val json = write(disciplinas)

  val writer = new PrintWriter(new File(nome+".json"))
  
  try {
    writer.write(json)
    return true
  } catch {
    case e: Throwable => {
      println(e.toString()) 
      return false
      }
  }  
}

}