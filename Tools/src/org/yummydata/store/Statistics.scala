package org.yummydata.store

import java.util.Calendar
import org.openrdf.query.Query
import org.yummydata.store.Query

case class Statistic(val sparql: String, val key: String)

object CustomStats {
  import Statistics._

  def main(args: Array[String]) {
        val q1 = "prefix meta:<http://yummydata.org/meta#> " +
      "prefix rdfs:<http://www.w3.org/2000/01/rdf-schema#> " + 
      "select ?name ?endpoint ?query ?measure where { " +
      "?endp meta:hasEndpointString ?endpoint . " +
      "?endp meta:customMeasure ?measure ." +
      "?measure rdfs:label ?name . " +
      "?measure meta:sparqlQuery ?query . }";
    
    val r = Query.doQuery(q1)
    var stats: List[(String, String, String, String)] = Nil
    while (r.hasNext) {
      val n = r.next
      val i = n.iterator
      stats ::= ((i.next.getValue.stringValue, i.next.getValue.stringValue,
          i.next.getValue.stringValue, i.next.getValue.stringValue.split(".org/")(1)))
    }
    
    for ((n, endp, q, measure) <- stats.par) {
      println(n + " " + endp)
      val r = Query.doQuery(endp, q)
      val v = r.next.iterator.next.getValue.stringValue
      insertStat(endp, measure, v)
      println(v)
    }
  }
}

object MainStats {
  import Statistics._
     
  def main(args: Array[String]) {
        println(getURIs)

    for (u <- getURIs.toSeq.par) {
//      for (u <- ug.reverse) {
        println(u)
        for (q <- queries) {
          try {
            val r = Query.doQuery(u, q.sparql)
            val v = r.next.iterator.next.getValue.stringValue
            
            insertStat(u, q.key, v)
            println(v)
          } catch {
            case e: Exception => {
              e.printStackTrace
            }
          }
        }
//      }
    }
  }
}

object Statistics {
  val queries:List[Statistic] = List(
      Statistic("select (count(distinct ?s) as ?n) where { ?s ?p ?o . } ", "measures/numSubjects"),
      Statistic("select (count(distinct ?p) as ?n) { ?s ?p ?o . } ", "measures/numPredicates"),
      Statistic("select (count(distinct ?o) as ?n) { ?s ?p ?o . } ", "measures/numObjects"),
      Statistic("SELECT (COUNT(*) AS ?no) { ?s ?p ?o  }", "measures/numTriples")
      )
      

  //Compute date once, so it remains the same throughout the run
  val dat = {
    val c = Calendar.getInstance()
    "%02d-%02d-%02d".format(c.get(Calendar.YEAR),
      c.get(Calendar.MONTH) + 1,
      c.get(Calendar.DAY_OF_MONTH))
  }      
  
  def insertStat(uri: String, measure: String, value: String): Unit = synchronized {
    //work out today's date string
    
        Query.doUpdate("insert { graph <http://yummydata.org/data/" + dat + "> " +
            "{ ?endp <http://yummydata.org/" + measure + "> " +
        		"\"" + value + "\" } } where { ?endp  <http://yummydata.org/meta#hasEndpointString> " +
        				"\"" + uri + "\" }")
  }
  
  def getURIs: Seq[String] = {
    val r = Query.doQuery("select ?uri where { ?endp <http://yummydata.org/meta#hasEndpointString> ?uri }")
    var uris: List[String] = Nil
        
    while (r.hasNext) {
      val n = r.next
      uris ::= n.iterator.next.getValue.stringValue
    }
    uris.reverse
  }

}