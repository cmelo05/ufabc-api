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
class Disciplina (val co: String, val an: Int, val dis: String, val cat: String,
    val cred: Int, val sit: String, val per: Int, val con: String){

  /**
   * Override to return a CSV model
   */
  override def toString(): String = co+";"+an+";"+dis+";"+cat+";"+cred+";"+sit+";"+per+";"+con
  
}