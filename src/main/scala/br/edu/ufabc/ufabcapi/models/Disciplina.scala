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
trait Disciplina {
  
  def codigo: String
  def ano: Int
  def disciplina: String
  def categoria: String
  def creditos: Int
  def situacao: Option[String]
  def periodo: String
  def conceito: String
  
  /**
 	* Override para retonar uma linha de CSV
 	*/
  override def toString(): String = codigo+";"+ano+";"+disciplina+";"+categoria+";"
  +creditos+";"+situacao+";"+periodo+";"+conceito     
  
    /**
   * Entrada: Conceito do aluno em String; Creditos da disciplina em Int
   * Saida: Float dessa operacao
   * 
   * Metodo utiliza o Pattern Matching para retornar o calculo correto
   */
  def CalculoUnitario(): Float ={
    val conceitoA: Int = 4
    val conceitoB: Int = 3
    val conceitoC: Int = 2
    val conceitoD: Int = 1
    val conceitoReprova: Int = 0
    
    this.conceito match{
      case "A" => conceitoA * this.creditos
      case "B" => conceitoB * this.creditos
      case "C" => conceitoC * this.creditos
      case "D" => conceitoD * this.creditos
      case _ => conceitoReprova * this.creditos
    }
  }
}