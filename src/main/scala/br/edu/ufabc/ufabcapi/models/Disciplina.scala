package main.scala.br.edu.ufabc.ufabcapi.models

/**
 * Class created to handle the JSON object.
 *  "codigo": "BC0001",
    "ano": 2010,
    "disciplina": "Base Experimental das Ciências Naturais",
    "categoria": "Obrigatória",
    "creditos": 3,
    "situacao": "Aprovado",
    "periodo": "2",
    "conceito": "B"
 */
class Disciplina (val codigo: String, val ano: Int, val disciplina: String, val categoria: String,
                  val creditos: Int, val situacao: String, val periodo: Int, val conceito: String ) {
  
  /**
 	* Override to return a CSV model
 	*/
  override def toString(): String = codigo+";"+ano+";"+disciplina+";"+categoria+";"
  +creditos+";"+situacao+";"+periodo+";"+conceito     
}