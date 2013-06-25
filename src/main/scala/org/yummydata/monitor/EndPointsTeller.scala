package org.yummydata.monitor
/**
 * Returns a list of endpoint.
 * Queries the yummyData endpoint now, but could directly try to access CKAN.
 */
object EndPointsTeller {
	val yummyEndpoint="http://yummydata.org:3030/yummy/";
	def getEndpointsList(): Array[String] = {
	  val dummyResult=Array(
	      "http://dbpedia.org/sparql",
	      "http://sparql.wikipathways.org",
	      "http://data.allie.dbcls.jp/sparql",
	      "http://data.nature.com/sparql",
	      "http://biocyc.bio2rdf.org/sparql",
	      "http://drugbank.bio2rdf.org/sparql",
	      "http://go.bio2rdf.org/sparql",
	      "http://genbank.bio2rdf.org/sparql",
	      "http://citeseer.rkbexplorer.com/sparql/",
	      "http://beta.sparql.uniprot.org"
	    )
		return dummyResult;
	}
}
	
